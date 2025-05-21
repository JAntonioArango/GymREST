package com.epam.gymapp.credentialsTest.reposTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.epam.gymapp.entities.Trainee;
import com.epam.gymapp.entities.Trainer;
import com.epam.gymapp.entities.Training;
import com.epam.gymapp.entities.TrainingType;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.TrainingRepo;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class TrainingRepoTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private TrainingRepo trainingRepo;

  private User traineeUser;
  private User trainerUser;
  private Trainee trainee;
  private Trainer trainer;
  private TrainingType trainingType;
  private Training training1;
  private Training training2;

  @BeforeEach
  void setUp() {
    // Create a trainee
    traineeUser = new User();
    traineeUser.setUsername("trainee1");
    traineeUser.setPassword("pass123");
    traineeUser.setFirstName("John");
    traineeUser.setLastName("Doe");
    entityManager.persist(traineeUser);

    trainee = new Trainee();
    trainee.setUser(traineeUser);
    trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
    trainee.setAddress("123 Main St");
    entityManager.persist(trainee);

    // Create a trainer
    trainerUser = new User();
    trainerUser.setUsername("trainer1");
    trainerUser.setPassword("pass456");
    trainerUser.setFirstName("Sarah");
    trainerUser.setLastName("Smith");
    entityManager.persist(trainerUser);

    trainingType = new TrainingType();
    trainingType.setName("Cardio");
    entityManager.persist(trainingType);

    trainer = new Trainer();
    trainer.setUser(trainerUser);
    entityManager.persist(trainer);

    // Create trainings
    training1 = new Training();
    training1.setTrainee(trainee);
    training1.setTrainer(trainer);
    training1.setTrainingType(trainingType);
    training1.setTrainingName("Morning Run");
    training1.setTrainingDate(LocalDate.of(2023, 5, 15));
    training1.setTrainingDuration(60);
    entityManager.persist(training1);

    training2 = new Training();
    training2.setTrainee(trainee);
    training2.setTrainer(trainer);
    training2.setTrainingType(trainingType);
    training2.setTrainingName("Evening Circuit");
    training2.setTrainingDate(LocalDate.of(2023, 5, 20));
    training2.setTrainingDuration(45);
    entityManager.persist(training2);

    entityManager.flush();
  }

  @Test
  void findByTraineeUserUsername_ShouldReturnTrainingsForTrainee() {
    // When
    Pageable pageable = PageRequest.of(0, 10);
    Page<Training> trainings = trainingRepo.findByTraineeUserUsername("trainee1", pageable);

    // Then
    assertThat(trainings.getTotalElements()).isEqualTo(2);
    assertThat(trainings.getContent())
        .extracting(Training::getTrainingName)
        .containsExactlyInAnyOrder("Morning Run", "Evening Circuit");
  }

  @Test
  void findByTrainerUserUsername_ShouldReturnTrainingsForTrainer() {
    // When
    List<Training> trainings = trainingRepo.findByTrainerUserUsername("trainer1");

    // Then
    assertThat(trainings).hasSize(2);
    assertThat(trainings)
        .extracting(Training::getTrainingName)
        .containsExactlyInAnyOrder("Morning Run", "Evening Circuit");
  }
}
