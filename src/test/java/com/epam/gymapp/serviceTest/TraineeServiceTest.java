package com.epam.gymapp.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.api.dto.CreateTraineeDto;
import com.epam.gymapp.api.dto.TraineeDto;
import com.epam.gymapp.entities.Trainee;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.TraineeRepo;
import com.epam.gymapp.repositories.TrainerRepo;
import com.epam.gymapp.repositories.UserRepository;
import com.epam.gymapp.services.TraineeService;
import com.epam.gymapp.utils.CredentialGenerator;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

  @Mock private TraineeRepo traineeRepo;
  @Mock private TrainerRepo trainerRepo;
  @Mock private UserRepository userRepository;
  @Mock private CredentialGenerator creds;
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private TraineeService service;

  private CreateTraineeDto dto;
  private User savedUser;
  private Trainee savedTrainee;

  @BeforeEach
  void init() {
    dto = new CreateTraineeDto("John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St");

    lenient().when(passwordEncoder.encode(any())).thenReturn("$2a$dummyhash");

    savedUser = new User();
    savedUser.setUsername("john.doe-abc123");
    savedUser.setPassword("plainPass");

    savedTrainee = new Trainee();
    savedTrainee.setUser(savedUser);
    savedTrainee.setDateOfBirth(dto.dateOfBirth());
    savedTrainee.setAddress(dto.address());
  }

  @Test
  void createProfile_shouldSaveAndReturnDto() {
    when(creds.buildUniqueUsername("John", "Doe")).thenReturn("jdoe");
    when(creds.randomPassword()).thenReturn("ignoredRawPwd");

    when(traineeRepo.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

    TraineeDto result = service.createProfile(dto);

    assertNotNull(result);
    assertEquals("jdoe", result.username());
    assertEquals("John", result.firstName());
    assertEquals("Doe", result.lastName());
    assertTrue(result.active());
    verify(creds).buildUniqueUsername("John", "Doe");
    verify(traineeRepo).save(any(Trainee.class));
  }

  @Test
  void register_shouldThrowWhenTrainerExists() {
    when(trainerRepo.existsByUserFirstNameAndUserLastName("John", "Doe")).thenReturn(true);

    assertThrows(ApiException.class, () -> service.register(dto));
    verify(trainerRepo).existsByUserFirstNameAndUserLastName("John", "Doe");
  }
}
