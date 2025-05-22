package com.epam.gymapp.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.api.dto.*;
import com.epam.gymapp.entities.*;
import com.epam.gymapp.repositories.*;
import com.epam.gymapp.services.TraineeService;
import com.epam.gymapp.utils.CredentialGenerator;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class TraineeServiceUnitTest {

  @Mock private TraineeRepo traineeRepo;
  @Mock private CredentialGenerator creds;
  @Mock private TrainerRepo trainerRepo;
  @Mock private UserRepository userRepository;

  @InjectMocks private TraineeService service;

  private CreateTraineeDto createDto;
  private User savedUser;
  private Trainee savedTrainee;

  @BeforeEach
  void setUp() {
    createDto = new CreateTraineeDto("John", "Doe", LocalDate.of(1990, 1, 1), "123 Street");

    savedUser = new User();
    savedUser.setUsername("john.doe-abc123");
    savedUser.setPassword("pass123");
    savedUser.setFirstName("John");
    savedUser.setLastName("Doe");
    savedUser.setActive(true);

    savedTrainee = new Trainee();
    savedTrainee.setUser(savedUser);
    savedTrainee.setDateOfBirth(createDto.dateOfBirth());
    savedTrainee.setAddress(createDto.address());
  }

  @Test
  void register_shouldThrowWhenTrainerExists() {
    when(trainerRepo.existsByUserFirstNameAndUserLastName("John", "Doe")).thenReturn(true);

    ApiException ex = assertThrows(ApiException.class, () -> service.register(createDto));
    assertEquals(400, ex.getStatus().value());
  }

  @Test
  void register_shouldCreateProfileAndReturnCredentials() {
    when(trainerRepo.existsByUserFirstNameAndUserLastName(anyString(), anyString()))
        .thenReturn(false);
    // stub createProfile internals
    when(creds.buildUniqueUsername("John", "Doe")).thenReturn("john.doe-abc123");
    when(creds.randomPassword()).thenReturn("pass123");
    when(traineeRepo.save(any(Trainee.class))).thenReturn(savedTrainee);
    when(userRepository.findByUsername("john.doe-abc123")).thenReturn(Optional.of(savedUser));

    TraineeRegistrationDto dto = service.register(createDto);

    assertEquals("john.doe-abc123", dto.username());
    assertEquals("pass123", dto.password());
    verify(traineeRepo).save(any());
  }

  @Test
  void createProfile_shouldSaveAndReturnDto() {
    when(creds.buildUniqueUsername("John", "Doe")).thenReturn("john.x123");
    when(creds.randomPassword()).thenReturn("pwd123");

    when(traineeRepo.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

    TraineeDto dto = service.createProfile(createDto);

    assertEquals("john.x123", dto.username());
    assertEquals("John", dto.firstName());
    assertEquals("Doe", dto.lastName());
    assertEquals(createDto.dateOfBirth(), dto.dateOfBirth());
    assertTrue(dto.active());
  }

  @Test
  void updateProfile_shouldModifyAndReturnProfile() {
    UpdateTraineeDto upd = new UpdateTraineeDto("Alice", "Smith", null, "New Addr", false);
    Trainee t = new Trainee();
    User u = new User();
    u.setFirstName("Old");
    u.setLastName("Old");
    t.setUser(u);
    when(traineeRepo.findByUserUsername("alice")).thenReturn(Optional.of(t));

    TraineeProfileDto profile = service.updateProfile("alice", upd);

    assertEquals("Alice", profile.firstName());
    assertEquals("Smith", profile.lastName());
    assertEquals("New Addr", profile.address());
    assertFalse(profile.isActive());
  }

  @Test
  void deleteByUsername_shouldDetachAndDelete() {
    Trainee t = spy(new Trainee());
    when(traineeRepo.findByUserUsername("u")).thenReturn(Optional.of(t));

    service.deleteByUsername("u");

    verify(traineeRepo).delete(t);
  }

  @Test
  void setActive_shouldToggleAndReturnDto() {
    Trainee t = new Trainee();
    User u = new User();
    u.setActive(false);
    t.setUser(u);
    when(traineeRepo.findByUserUsername("u")).thenReturn(Optional.of(t));

    TraineeDto dto = service.setActive("u", true);
    assertTrue(dto.active());
  }

  @Test
  void list_shouldReturnPagedDtos() {
    Trainee t = new Trainee();
    User u = new User();
    u.setUsername("x");
    u.setFirstName("F");
    u.setLastName("L");
    u.setActive(true);
    t.setUser(u);
    t.setDateOfBirth(LocalDate.of(2000, 1, 1));
    t.setAddress("A");
    when(traineeRepo.findAll(PageRequest.of(0, 2))).thenReturn(new PageImpl<>(List.of(t)));

    var page = service.list(PageRequest.of(0, 2));
    assertEquals(1, page.getTotalElements());
    assertEquals("x", page.getContent().get(0).username());
  }

  @Test
  void findProfile_shouldMapToProfileDto() {
    Trainee t = new Trainee();
    User u = new User();
    u.setUsername("u");
    u.setFirstName("F");
    u.setLastName("L");
    u.setActive(true);
    t.setUser(u);
    t.setDateOfBirth(LocalDate.now());
    t.setAddress("Addr");
    when(traineeRepo.findByUserUsername("u")).thenReturn(Optional.of(t));

    TraineeProfileDto pd = service.findProfile("u");
    assertEquals("u", pd.userName());
    assertTrue(pd.trainers().isEmpty());
  }

  @Test
  void replaceTrainers_shouldReplaceAndReturnShortDtos() {
    Trainee t = new Trainee();
    when(traineeRepo.findByUserUsername("u")).thenReturn(Optional.of(t));
    Trainer tr1 = new Trainer();
    User u1 = new User();
    u1.setUsername("a");
    u1.setFirstName("A");
    u1.setLastName("B");
    tr1.setUser(u1);
    tr1.setSpecialization(new TrainingType());
    when(trainerRepo.findByUserUsernameIn(List.of("a"))).thenReturn(List.of(tr1));

    var out = service.replaceTrainers("u", List.of("a"));
    assertThat(out).hasSize(1).extracting(TrainerShortDto::username).containsExactly("a");
  }

  @Test
  void replaceTrainers_shouldThrowWhenMissing() {
    Trainee t = new Trainee();
    when(traineeRepo.findByUserUsername("u")).thenReturn(Optional.of(t));
    when(trainerRepo.findByUserUsernameIn(List.of("a", "b"))).thenReturn(List.of());
    assertThrows(ApiException.class, () -> service.replaceTrainers("u", List.of("a", "b")));
  }
}
