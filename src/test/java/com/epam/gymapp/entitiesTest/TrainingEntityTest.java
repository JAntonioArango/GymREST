package com.epam.gymapp.entitiesTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.epam.gymapp.entities.Trainee;
import com.epam.gymapp.entities.Trainer;
import com.epam.gymapp.entities.Training;
import com.epam.gymapp.entities.TrainingType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainingEntityTest {
  private Training training;
  private Trainee trainee;
  private Trainer trainer;
  private TrainingType trainingType;
  private LocalDate testDate;

  @BeforeEach
  void setUp() {
    training = new Training();
    trainee = new Trainee();
    trainer = new Trainer();
    trainingType = new TrainingType();
    testDate = LocalDate.of(2024, 1, 1);
  }

  @Test
  void testNoArgsConstructor() {
    assertNotNull(training);
    assertNull(training.getId());
    assertNull(training.getTrainee());
    assertNull(training.getTrainer());
    assertNull(training.getTrainingType());
    assertNull(training.getTrainingName());
    assertNull(training.getTrainingDate());
    assertNull(training.getTrainingDuration());
  }

  @Test
  void testAllArgsConstructor() {
    Training training =
        new Training(1L, trainee, trainer, trainingType, "Test Training", testDate, 60);

    assertEquals(1L, training.getId());
    assertEquals(trainee, training.getTrainee());
    assertEquals(trainer, training.getTrainer());
    assertEquals(trainingType, training.getTrainingType());
    assertEquals("Test Training", training.getTrainingName());
    assertEquals(testDate, training.getTrainingDate());
    assertEquals(60, training.getTrainingDuration());
  }

  @Test
  void testSetAndGetId() {
    training.setId(1L);
    assertEquals(1L, training.getId());
  }

  @Test
  void testSetAndGetTrainee() {
    training.setTrainee(trainee);
    assertEquals(trainee, training.getTrainee());
  }

  @Test
  void testSetAndGetTrainer() {
    training.setTrainer(trainer);
    assertEquals(trainer, training.getTrainer());
  }

  @Test
  void testSetAndGetTrainingType() {
    training.setTrainingType(trainingType);
    assertEquals(trainingType, training.getTrainingType());
  }

  @Test
  void testSetAndGetTrainingName() {
    String trainingName = "Test Training Session";
    training.setTrainingName(trainingName);
    assertEquals(trainingName, training.getTrainingName());
  }

  @Test
  void testSetAndGetTrainingDate() {
    training.setTrainingDate(testDate);
    assertEquals(testDate, training.getTrainingDate());
  }

  @Test
  void testSetAndGetTrainingDuration() {
    Integer duration = 45;
    training.setTrainingDuration(duration);
    assertEquals(duration, training.getTrainingDuration());
  }

  @Test
  void testTrainingDateFormat() {
    LocalDate date = LocalDate.of(2024, 1, 1);
    training.setTrainingDate(date);
    assertEquals("2024-01-01", training.getTrainingDate().toString());
  }

  @Test
  void testValidTrainingDuration() {
    training.setTrainingDuration(30);
    assertTrue(training.getTrainingDuration() > 0);
  }

  @Test
  void testRequiredFieldsNotNull() {
    Training fullTraining =
        new Training(1L, trainee, trainer, trainingType, "Test Training", testDate, 60);

    assertNotNull(fullTraining.getTrainee(), "Trainee should not be null");
    assertNotNull(fullTraining.getTrainer(), "Trainer should not be null");
    assertNotNull(fullTraining.getTrainingType(), "Training type should not be null");
  }

  @Test
  void testContentEquality() {
    Training training1 =
        new Training(1L, trainee, trainer, trainingType, "Training 1", testDate, 60);
    Training training2 =
        new Training(1L, trainee, trainer, trainingType, "Training 1", testDate, 60);
    Training training3 =
        new Training(2L, trainee, trainer, trainingType, "Training 2", testDate, 45);

    assertThat(training1).usingRecursiveComparison().isEqualTo(training2);

    assertThat(training1).usingRecursiveComparison().isNotEqualTo(training3);
  }
}
