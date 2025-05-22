package com.epam.gymapp.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.api.dto.*;
import com.epam.gymapp.entities.*;
import com.epam.gymapp.repositories.*;
import com.epam.gymapp.services.TrainerService;
import com.epam.gymapp.utils.CredentialGenerator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class TrainerServiceUnitTest {

  @Mock private TrainerRepo trainerRepo;
  @Mock private TrainingTypeRepo typeRepo;
  @Mock private CredentialGenerator creds;
  @Mock private TraineeRepo traineeRepo;
  @Mock private UserRepository userRepository;

  @InjectMocks private TrainerService service;

  private CreateTrainerDto createDto;
  private TrainingType mockType;
  private User savedUser;
  private Trainer savedTrainer;

  @BeforeEach
  void setUp() {
    createDto = new CreateTrainerDto("John", "Doe", "YOGA");
    mockType = new TrainingType();
    mockType.setName("YOGA");

    savedUser = new User();
    savedUser.setUsername("john.doe-xyz");
    savedUser.setPassword("pwd");
    savedTrainer = new Trainer();
    savedTrainer.setUser(savedUser);
    savedTrainer.setSpecialization(mockType);
  }

  @Test
  void register_shouldThrowWhenTraineeExists() {
    when(traineeRepo.existsByUserFirstNameAndUserLastName("John", "Doe")).thenReturn(true);
    ApiException ex = assertThrows(ApiException.class, () -> service.register(createDto));
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
  }

  @Test
  void register_shouldCreateProfileAndReturnCredentials() {
    when(traineeRepo.existsByUserFirstNameAndUserLastName(any(), any())).thenReturn(false);
    when(creds.buildUniqueUsername("John", "Doe")).thenReturn("john.doe-xyz");
    when(creds.randomPassword()).thenReturn("pwd");
    when(typeRepo.findByName("YOGA")).thenReturn(Optional.of(mockType));
    when(trainerRepo.save(any(Trainer.class))).thenReturn(savedTrainer);
    when(userRepository.findByUsername("john.doe-xyz")).thenReturn(Optional.of(savedUser));

    TrainerRegistrationDto dto = service.register(createDto);
    assertEquals("john.doe-xyz", dto.username());
    assertEquals("pwd", dto.password());
  }

  @Test
  void createProfile_shouldSaveAndReturnDto() {
    when(typeRepo.findByName("YOGA")).thenReturn(Optional.of(mockType));
    when(creds.buildUniqueUsername("John", "Doe")).thenReturn("john.x123");
    when(creds.randomPassword()).thenReturn("pwd123");
    when(trainerRepo.save(any(Trainer.class))).thenReturn(savedTrainer);

    TrainerDto dto = service.createProfile(createDto);
    assertFalse(savedUser.getUsername().equals(dto.username()));
    assertEquals(mockType.getName(), dto.specialization());
  }

  @Test
  void findByUsername_shouldReturnDtoOrThrow() {
    when(trainerRepo.findByUserUsername("u")).thenReturn(Optional.of(savedTrainer));
    TrainerDto dto = service.findByUsername("u");
    assertFalse("u".equals(dto.username()));

    when(trainerRepo.findByUserUsername("x")).thenReturn(Optional.empty());
    assertThrows(ApiException.class, () -> service.findByUsername("x"));
  }

  @Test
  void updateProfile_shouldModifyAndReturnProfile() {
    UpdateTrainerDto upd = new UpdateTrainerDto("Alice", "Smith", "BOXING", false);
    savedTrainer.getUser().setFirstName("Old");
    savedTrainer.getUser().setLastName("Old");
    savedTrainer.getUser().setActive(true);
    when(trainerRepo.findByUserUsername("john")).thenReturn(Optional.of(savedTrainer));

    TrainerProfileDto pd = service.updateProfile("john", upd);
    assertEquals("Alice", pd.firstName());
    assertEquals("Smith", pd.lastName());
    assertFalse(pd.isActive());
  }

  @Test
  void changePassword_shouldValidateAndChangeOrThrow() {
    when(trainerRepo.existsByUserUsernameAndUserPassword("u", "old")).thenReturn(false);
    assertThrows(ApiException.class, () -> service.changePassword("u", "old", "new"));

    when(trainerRepo.existsByUserUsernameAndUserPassword("u", "old")).thenReturn(true);
    when(trainerRepo.findByUserUsername("u")).thenReturn(Optional.of(savedTrainer));
    service.changePassword("u", "old", "new");
    assertEquals("new", savedTrainer.getUser().getPassword());
  }

  @Test
  void setActive_shouldToggleAndReturnDto() {
    savedTrainer.getUser().setActive(false);
    when(trainerRepo.findByUserUsername("u")).thenReturn(Optional.of(savedTrainer));
    TrainerDto dto = service.setActive("u", true);
    assertTrue(dto.active());
  }

  @Test
  void list_shouldReturnPagedDtos() {
    TrainingType t = mockType;
    savedTrainer.setId(5L);
    when(trainerRepo.findAll(PageRequest.of(0, 2)))
        .thenReturn(new PageImpl<>(List.of(savedTrainer)));

    var page = service.list(PageRequest.of(0, 2));
    assertEquals(1, page.getTotalElements());
    assertEquals(5L, page.getContent().get(0).id());
  }

  @Test
  void findProfile_shouldMapToProfileDto() {
    when(trainerRepo.findByUserUsername("u")).thenReturn(Optional.of(savedTrainer));
    TrainerProfileDto pd = service.findProfile("u");
    assertFalse("u".equals(pd.username()));
    assertThat(pd.trainees()).isEmpty();
  }

  @Test
  void unassignedActiveTrainers_shouldFilterAndReturnShortDtos() {
    // trainer with id 1 assigned to trainee
    Trainer assigned = new Trainer();
    assigned.setId(1L);
    User assignedUser = new User();
    assignedUser.setUsername("assigned");
    assignedUser.setActive(true);
    assigned.setUser(assignedUser);
    when(trainerRepo.findByTraineesUserUsername("trainee")).thenReturn(List.of(assigned));

    // free trainer not assigned yet
    Trainer free = new Trainer();
    free.setId(2L);
    User freeUser = new User();
    freeUser.setUsername("free");
    freeUser.setFirstName("F");
    freeUser.setLastName("L");
    freeUser.setActive(true);
    free.setUser(freeUser);
    free.setSpecialization(mockType);
    when(trainerRepo.findAll()).thenReturn(List.of(assigned, free));

    List<TrainerShortDto> out = service.unassignedActiveTrainers("trainee");
    assertEquals(1, out.size());
    assertEquals("free", out.get(0).username());
  }
}
