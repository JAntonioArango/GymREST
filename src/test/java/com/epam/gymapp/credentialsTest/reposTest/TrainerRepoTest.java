package com.epam.gymapp.credentialsTest.reposTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.epam.gymapp.entities.Trainee;
import com.epam.gymapp.entities.Trainer;
import com.epam.gymapp.entities.TrainingType;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.TrainerRepo;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class TrainerRepoTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private TrainerRepo trainerRepo;

  @Test
  void findByUserUsername_ShouldReturnTrainer_WhenUsernameExists() {
    // Given
    User user = new User();
    user.setUsername("trainer1");
    user.setPassword("pass123");
    user.setFirstName("Alex");
    user.setLastName("Johnson");
    entityManager.persist(user);

    TrainingType trainingType = new TrainingType();
    trainingType.setName("Strength");
    entityManager.persist(trainingType);

    Trainer trainer = new Trainer();
    trainer.setUser(user);
    entityManager.persist(trainer);
    entityManager.flush();

    // When
    Optional<Trainer> foundTrainer = trainerRepo.findByUserUsername("trainer1");

    // Then
    assertThat(foundTrainer).isPresent();
    assertThat(foundTrainer.get().getUser().getUsername()).isEqualTo("trainer1");
  }

  @Test
  void findByUserUsername_ShouldReturnEmpty_WhenUsernameDoesNotExist() {
    // When
    Optional<Trainer> foundTrainer = trainerRepo.findByUserUsername("nonexistent");

    // Then
    assertThat(foundTrainer).isEmpty();
  }

  @Test
  void existsByUserUsernameAndUserPassword_ShouldReturnTrue_WhenCredentialsMatch() {
    // Given
    User user = new User();
    user.setUsername("trainer2");
    user.setPassword("secure456");
    user.setFirstName("Maria");
    user.setLastName("Garcia");
    entityManager.persist(user);

    TrainingType trainingType = new TrainingType();
    trainingType.setName("Yoga");
    entityManager.persist(trainingType);

    Trainer trainer = new Trainer();
    trainer.setUser(user);
    ;
    entityManager.persist(trainer);
    entityManager.flush();

    // When
    boolean exists = trainerRepo.existsByUserUsernameAndUserPassword("trainer2", "secure456");

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void existsByUserUsernameAndUserPassword_ShouldReturnFalse_WhenCredentialsDoNotMatch() {
    // Given
    User user = new User();
    user.setUsername("trainer3");
    user.setPassword("correct789");
    user.setFirstName("David");
    user.setLastName("Wang");
    entityManager.persist(user);

    TrainingType trainingType = new TrainingType();
    trainingType.setName("Cardio");
    entityManager.persist(trainingType);

    Trainer trainer = new Trainer();
    trainer.setUser(user);
    entityManager.persist(trainer);
    entityManager.flush();

    // When
    boolean exists = trainerRepo.existsByUserUsernameAndUserPassword("trainer3", "wrong789");

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void findByTraineesUserUsername_ShouldReturnTrainers_WhenUserExists() {
    // Given
    // Create a trainee
    User traineeUser = new User();
    traineeUser.setUsername("trainee1");
    traineeUser.setPassword("pass123");
    traineeUser.setFirstName("John");
    traineeUser.setLastName("Doe");
    entityManager.persist(traineeUser);

    Trainee trainee = new Trainee();
    trainee.setUser(traineeUser);
    trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
    trainee.setAddress("123 Main St");
    entityManager.persist(trainee);

    // Create first trainer
    User trainerUser1 = new User();
    trainerUser1.setUsername("trainer4");
    trainerUser1.setPassword("pass123");
    trainerUser1.setFirstName("Sarah");
    trainerUser1.setLastName("Smith");
    entityManager.persist(trainerUser1);

    TrainingType trainingType1 = new TrainingType();
    trainingType1.setName("Pilates");
    entityManager.persist(trainingType1);

    Trainer trainer1 = new Trainer();
    trainer1.setUser(trainerUser1);
    ;
    trainer1.setTrainees(new HashSet<>(Arrays.asList(trainee)));
    entityManager.persist(trainer1);

    // Create second trainer
    User trainerUser2 = new User();
    trainerUser2.setUsername("trainer5");
    trainerUser2.setPassword("pass123");
    trainerUser2.setFirstName("Michael");
    trainerUser2.setLastName("Brown");
    entityManager.persist(trainerUser2);

    TrainingType trainingType2 = new TrainingType();
    trainingType2.setName("CrossFit");
    entityManager.persist(trainingType2);

    Trainer trainer2 = new Trainer();
    trainer2.setUser(trainerUser2);
    trainer2.setTrainees(new HashSet<>(Arrays.asList(trainee)));
    entityManager.persist(trainer2);

    entityManager.flush();

    // When
    List<Trainer> trainers = trainerRepo.findByTraineesUserUsername("trainee1");

    // Then
    assertThat(trainers).hasSize(2);
    assertThat(trainers)
        .extracting(trainer -> trainer.getUser().getUsername())
        .containsExactlyInAnyOrder("trainer4", "trainer5");
  }

  @Test
  void findAll_ShouldReturnAllTrainers() {
    // Given
    // Create first trainer
    User trainerUser1 = new User();
    trainerUser1.setUsername("trainer6");
    trainerUser1.setPassword("pass123");
    trainerUser1.setFirstName("Carlos");
    trainerUser1.setLastName("Perez");
    entityManager.persist(trainerUser1);

    TrainingType trainingType1 = new TrainingType();
    trainingType1.setName("Swimming");
    entityManager.persist(trainingType1);

    Trainer trainer1 = new Trainer();
    trainer1.setUser(trainerUser1);
    entityManager.persist(trainer1);

    // Create second trainer
    User trainerUser2 = new User();
    trainerUser2.setUsername("trainer7");
    trainerUser2.setPassword("pass123");
    trainerUser2.setFirstName("Lisa");
    trainerUser2.setLastName("Chen");
    entityManager.persist(trainerUser2);

    TrainingType trainingType2 = new TrainingType();
    trainingType2.setName("Boxing");
    entityManager.persist(trainingType2);

    Trainer trainer2 = new Trainer();
    trainer2.setUser(trainerUser2);
    entityManager.persist(trainer2);

    entityManager.flush();

    // When
    List<Trainer> trainers = trainerRepo.findAll();

    // Then
    assertThat(trainers).hasSize(2);
    assertThat(trainers)
        .extracting(trainer -> trainer.getUser().getUsername())
        .containsExactlyInAnyOrder("trainer6", "trainer7");
  }

  @Test
  void findByUserUsernameIn_ShouldReturnMatchingTrainers() {
    // Given
    // Create first trainer
    User trainerUser1 = new User();
    trainerUser1.setUsername("trainer8");
    trainerUser1.setPassword("pass123");
    trainerUser1.setFirstName("Frank");
    trainerUser1.setLastName("Wilson");
    entityManager.persist(trainerUser1);

    TrainingType trainingType1 = new TrainingType();
    trainingType1.setName("Running");
    entityManager.persist(trainingType1);

    Trainer trainer1 = new Trainer();
    trainer1.setUser(trainerUser1);
    entityManager.persist(trainer1);

    // Create second trainer
    User trainerUser2 = new User();
    trainerUser2.setUsername("trainer9");
    trainerUser2.setPassword("pass123");
    trainerUser2.setFirstName("Emma");
    trainerUser2.setLastName("Taylor");
    entityManager.persist(trainerUser2);

    TrainingType trainingType2 = new TrainingType();
    trainingType2.setName("HIIT");
    entityManager.persist(trainingType2);

    Trainer trainer2 = new Trainer();
    trainer2.setUser(trainerUser2);
    entityManager.persist(trainer2);

    // Create third trainer (not in search list)
    User trainerUser3 = new User();
    trainerUser3.setUsername("trainer10");
    trainerUser3.setPassword("pass123");
    trainerUser3.setFirstName("Robert");
    trainerUser3.setLastName("Lee");
    entityManager.persist(trainerUser3);

    TrainingType trainingType3 = new TrainingType();
    trainingType3.setName("Weight Lifting");
    entityManager.persist(trainingType3);

    Trainer trainer3 = new Trainer();
    trainer3.setUser(trainerUser3);
    entityManager.persist(trainer3);

    entityManager.flush();

    // When
    List<Trainer> trainers =
        trainerRepo.findByUserUsernameIn(Arrays.asList("trainer8", "trainer9"));

    // Then
    assertThat(trainers).hasSize(2);
    assertThat(trainers)
        .extracting(trainer -> trainer.getUser().getUsername())
        .containsExactlyInAnyOrder("trainer8", "trainer9");
  }

  @Test
  void existsByUserFirstNameAndUserLastName_ShouldReturnTrue_WhenNameExists() {
    // Given
    User user = new User();
    user.setUsername("trainer11");
    user.setPassword("pass456");
    user.setFirstName("Paul");
    user.setLastName("Anderson");
    entityManager.persist(user);

    TrainingType trainingType = new TrainingType();
    trainingType.setName("Functional");
    entityManager.persist(trainingType);

    Trainer trainer = new Trainer();
    trainer.setUser(user);
    entityManager.persist(trainer);
    entityManager.flush();

    // When
    boolean exists = trainerRepo.existsByUserFirstNameAndUserLastName("Paul", "Anderson");

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void existsByUserFirstNameAndUserLastName_ShouldReturnFalse_WhenNameDoesNotExist() {
    // When
    boolean exists = trainerRepo.existsByUserFirstNameAndUserLastName("Unknown", "Person");

    // Then
    assertThat(exists).isFalse();
  }
}
