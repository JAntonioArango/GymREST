package com.epam.gymapp.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.api.dto.CreateTraineeDto;
import com.epam.gymapp.api.dto.TraineeDto;
import com.epam.gymapp.api.dto.TraineeRegistrationDto;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

  @Mock private TraineeRepo traineeRepo;

  @Mock private CredentialGenerator creds;

  @Mock private TrainerRepo trainerRepo;

  @Mock private UserRepository userRepository;

  @InjectMocks private TraineeService service;

  private CreateTraineeDto dto;

  @BeforeEach
  void init() {
    dto = new CreateTraineeDto("John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St");
  }

  @Test
  void createProfile_shouldSaveAndReturnDto() {
    // arrange
    when(creds.buildUniqueUsername("John", "Doe")).thenReturn("jdoe");
    when(traineeRepo.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

    // act
    TraineeDto result = service.createProfile(dto);

    // assert
    assertNotNull(result);
    assertEquals("jdoe", result.username());
    assertEquals("John", result.firstName());
    verify(creds).buildUniqueUsername("John", "Doe");
    verify(traineeRepo).save(any(Trainee.class));
  }

  @Test
  void register_shouldThrowWhenTrainerExists() {
    // arrange
    when(trainerRepo.existsByUserFirstNameAndUserLastName("John", "Doe")).thenReturn(true);

    // act & assert
    assertThrows(ApiException.class, () -> service.register(dto));
    verify(trainerRepo).existsByUserFirstNameAndUserLastName("John", "Doe");
  }

  @Test
  void register_shouldReturnCredentialsWhenSuccess() {
    // arrange
    when(trainerRepo.existsByUserFirstNameAndUserLastName(any(), any())).thenReturn(false);
    service = spy(service);
    TraineeDto saved =
        new TraineeDto("jdoe", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", true);
    doReturn(saved).when(service).createProfile(dto);
    User user = new User();
    user.setUsername("jdoe");
    user.setPassword("pass123");
    when(userRepository.findByUsername("jdoe")).thenReturn(java.util.Optional.of(user));

    // act
    TraineeRegistrationDto reg = service.register(dto);

    // assert
    assertEquals("jdoe", reg.username());
    assertEquals("pass123", reg.password());
    verify(service).createProfile(dto);
    verify(userRepository).findByUsername("jdoe");
  }
}
