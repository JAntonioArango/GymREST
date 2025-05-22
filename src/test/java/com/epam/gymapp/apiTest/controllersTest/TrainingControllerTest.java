package com.epam.gymapp.apiTest.controllersTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.api.controllers.TrainingController;
import com.epam.gymapp.api.dto.*;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.services.TrainingService;
import com.epam.gymapp.utils.ApiListWrapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

  @Mock private TrainingService trainingService;

  @InjectMocks private TrainingController trainingController;

  @Mock private User mockUser;

  @Test
  void add_ShouldCallServiceAndReturnOk() {
    // Arrange
    CreateTrainingDto createTrainingDto =
        new CreateTrainingDto(
            "Daniela.Lopez123", "Sara.Maria123", "STRENGTH", LocalDate.of(2025, 5, 22), 15);

    // Act
    ResponseEntity<Void> response = trainingController.add(createTrainingDto, mockUser);

    // Assert
    verify(trainingService).addTraining(createTrainingDto);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void traineeTrainings_ShouldReturnTraineeTrainings() {
    // Arrange
    String username = "testUser";
    LocalDate from = LocalDate.now();
    LocalDate to = LocalDate.now().plusDays(7);
    String trainerName = "trainer1";
    String trainingType = "type1";
    int page = 0;
    int size = 20;

    List<TraineeTrainingDto> trainings = List.of(/* initialize with test data */ );
    Page<TraineeTrainingDto> trainingPage = new PageImpl<>(trainings);

    when(trainingService.traineeTrainings(
            eq(username),
            argThat(
                filter ->
                    filter.fromDate().equals(from)
                        && filter.toDate().equals(to)
                        && filter.trainerNameOrNull().equals(trainerName)
                        && filter.trainingType().equals(trainingType)),
            eq(PageRequest.of(page, size))))
        .thenReturn(trainingPage);

    // Act
    ApiListWrapper<TraineeTrainingDto> result =
        trainingController.traineeTrainings(
            username, from, to, trainerName, trainingType, page, size, mockUser);

    // Assert
    assertNotNull(result);
    assertEquals(trainings, result.items());
    verify(trainingService)
        .traineeTrainings(
            eq(username),
            any(TrainingService.TrainingFilter.class),
            eq(PageRequest.of(page, size)));
  }

  @Test
  void trainerTrainings_ShouldReturnTrainerTrainings() {
    // Arrange
    String username = "testTrainer";
    LocalDate from = LocalDate.now();
    LocalDate to = LocalDate.now().plusDays(7);
    String traineeName = "trainee1";
    int page = 0;
    int size = 20;

    List<TrainerTrainingDto> trainings = List.of(/* initialize with test data */ );
    Page<TrainerTrainingDto> trainingPage = new PageImpl<>(trainings);

    when(trainingService.trainerTrainings(
            eq(username),
            any(TrainingService.TrainingFilter.class),
            eq(PageRequest.of(page, size))))
        .thenReturn(trainingPage);

    // Act
    ApiListWrapper<TrainerTrainingDto> result =
        trainingController.trainerTrainings(username, from, to, traineeName, page, size, mockUser);

    // Assert
    assertNotNull(result);
    assertEquals(trainings, result.items());
    verify(trainingService)
        .trainerTrainings(
            eq(username),
            any(TrainingService.TrainingFilter.class),
            eq(PageRequest.of(page, size)));
  }

  @Test
  void listTypes_ShouldReturnTrainingTypes() {
    // Arrange
    List<TrainingTypeDto> types = List.of(/* initialize with test data */ );
    when(trainingService.listTrainingTypes()).thenReturn(types);

    // Act
    ApiListWrapper<TrainingTypeDto> result = trainingController.listTypes(mockUser);

    // Assert
    assertNotNull(result);
    assertEquals(types, result.items());
    verify(trainingService).listTrainingTypes();
  }
}
