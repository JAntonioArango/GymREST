package com.epam.gymapp.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.api.dto.CreateTrainerDto;
import com.epam.gymapp.api.dto.TrainerDto;
import com.epam.gymapp.entities.Specialization;
import com.epam.gymapp.entities.Trainer;
import com.epam.gymapp.entities.TrainingType;
import com.epam.gymapp.repositories.TraineeRepo;
import com.epam.gymapp.repositories.TrainerRepo;
import com.epam.gymapp.repositories.TrainingTypeRepo;
import com.epam.gymapp.repositories.UserRepository;
import com.epam.gymapp.services.TrainerService;
import com.epam.gymapp.utils.CredentialGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

  @Mock private TrainerRepo trainerRepo;
  @Mock private TrainingTypeRepo typeRepo;
  @Mock private CredentialGenerator creds;
  @Mock private TraineeRepo traineeRepo;
  @Mock private UserRepository userRepository;

  @InjectMocks private TrainerService service;
  private CreateTrainerDto dto;

  @BeforeEach
  void init() {
    dto = new CreateTrainerDto("Alice", "Smith", Specialization.CARDIO);
  }

  @Test
  void createProfile_shouldLoadTypeSaveAndReturnDto() {
    when(creds.buildUniqueUsername("Alice", "Smith")).thenReturn("asmith");
    when(typeRepo.findByName(Specialization.CARDIO))
        .thenReturn(Optional.of(new TrainingType(1L, Specialization.CARDIO, null)));
    when(trainerRepo.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));

    TrainerDto result = service.createProfile(dto);

    assertNotNull(result);
    assertEquals("asmith", result.username());
    assertEquals("CARDIO", result.specialization().toString());
    verify(creds).buildUniqueUsername("Alice", "Smith");
    verify(trainerRepo).save(any(Trainer.class));
  }

  @Test
  void register_shouldThrowWhenTraineeExists() {

    when(traineeRepo.existsByUserFirstNameAndUserLastName("Alice", "Smith")).thenReturn(true);

    assertThrows(ApiException.class, () -> service.register(dto));
    verify(traineeRepo).existsByUserFirstNameAndUserLastName("Alice", "Smith");
  }
}
