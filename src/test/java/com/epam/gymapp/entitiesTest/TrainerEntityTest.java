package com.epam.gymapp.entitiesTest;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gymapp.entities.Trainee;
import com.epam.gymapp.entities.Trainer;
import com.epam.gymapp.entities.Training;
import com.epam.gymapp.entities.TrainingType;
import com.epam.gymapp.entities.User;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainerEntityTest {

  private Trainer trainer;
  private User user;
  private TrainingType specialization;

  @BeforeEach
  void setUp() {
    trainer = new Trainer();
    user = new User(); // You'll need to create a minimal User class for testing
    specialization =
        new TrainingType(); // You'll need to create a minimal TrainingType class for testing
  }

  @Test
  void testNoArgsConstructor() {
    assertNotNull(trainer);
    assertNotNull(trainer.getTrainees());
    assertNotNull(trainer.getTrainings());
    assertTrue(trainer.getTrainees().isEmpty());
    assertTrue(trainer.getTrainings().isEmpty());
  }

  @Test
  void testAllArgsConstructor() {
    Set<Trainee> trainees = new HashSet<>();
    Set<Training> trainings = new HashSet<>();

    Trainer trainer = new Trainer(1L, specialization, user, trainees, trainings);

    assertEquals(1L, trainer.getId());
    assertEquals(specialization, trainer.getSpecialization());
    assertEquals(user, trainer.getUser());
    assertEquals(trainees, trainer.getTrainees());
    assertEquals(trainings, trainer.getTrainings());
  }

  @Test
  void testSetAndGetId() {
    trainer.setId(1L);
    assertEquals(1L, trainer.getId());
  }

  @Test
  void testSetAndGetSpecialization() {
    trainer.setSpecialization(specialization);
    assertEquals(specialization, trainer.getSpecialization());
  }

  @Test
  void testSetAndGetUser() {
    trainer.setUser(user);
    assertEquals(user, trainer.getUser());
  }

  @Test
  void testAddAndRemoveTrainee() {
    Trainee trainee = new Trainee(); // You'll need to create a minimal Trainee class for testing

    trainer.getTrainees().add(trainee);
    assertEquals(1, trainer.getTrainees().size());
    assertTrue(trainer.getTrainees().contains(trainee));

    trainer.getTrainees().remove(trainee);
    assertEquals(0, trainer.getTrainees().size());
    assertFalse(trainer.getTrainees().contains(trainee));
  }

  @Test
  void testAddAndRemoveTraining() {
    Training training =
        new Training(); // You'll need to create a minimal Training class for testing

    trainer.getTrainings().add(training);
    assertEquals(1, trainer.getTrainings().size());
    assertTrue(trainer.getTrainings().contains(training));

    trainer.getTrainings().remove(training);
    assertEquals(0, trainer.getTrainings().size());
    assertFalse(trainer.getTrainings().contains(training));
  }

  @Test
  void testTraineesInitializedAsEmptySet() {
    Trainer newTrainer = new Trainer();
    assertNotNull(newTrainer.getTrainees());
    assertTrue(newTrainer.getTrainees().isEmpty());
  }

  @Test
  void testTrainingsInitializedAsEmptySet() {
    Trainer newTrainer = new Trainer();
    assertNotNull(newTrainer.getTrainings());
    assertTrue(newTrainer.getTrainings().isEmpty());
  }
}
