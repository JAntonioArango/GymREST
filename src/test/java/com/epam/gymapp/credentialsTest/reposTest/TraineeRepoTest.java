package com.epam.gymapp.credentialsTest.reposTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.epam.gymapp.entities.Trainee;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.TraineeRepo;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class TraineeRepoTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private TraineeRepo traineeRepo;

  @Test
  void findByUserUsername_ShouldReturnTrainee_WhenUsernameExists() {
    // Given
    User user = new User();
    user.setUsername("trainee1");
    user.setPassword("pass123");
    user.setFirstName("John");
    user.setLastName("Doe");
    entityManager.persist(user);

    Trainee trainee = new Trainee();
    trainee.setUser(user);
    trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
    trainee.setAddress("123 Gym Street");
    entityManager.persist(trainee);
    entityManager.flush();

    // When
    Optional<Trainee> foundTrainee = traineeRepo.findByUserUsername("trainee1");

    // Then
    assertThat(foundTrainee).isPresent();
    assertThat(foundTrainee.get().getUser().getUsername()).isEqualTo("trainee1");
    assertThat(foundTrainee.get().getAddress()).isEqualTo("123 Gym Street");
  }

  @Test
  void findByUserUsername_ShouldReturnEmpty_WhenUsernameDoesNotExist() {
    // When
    Optional<Trainee> foundTrainee = traineeRepo.findByUserUsername("nonexistent");

    // Then
    assertThat(foundTrainee).isEmpty();
  }

  @Test
  void existsByUserUsernameAndUserPassword_ShouldReturnTrue_WhenCredentialsMatch() {
    // Given
    User user = new User();
    user.setUsername("trainee2");
    user.setPassword("secure123");
    user.setFirstName("Jane");
    user.setLastName("Smith");
    entityManager.persist(user);

    Trainee trainee = new Trainee();
    trainee.setUser(user);
    trainee.setDateOfBirth(LocalDate.of(1992, 5, 15));
    trainee.setAddress("456 Fitness Ave");
    entityManager.persist(trainee);
    entityManager.flush();

    // When
    boolean exists = traineeRepo.existsByUserUsernameAndUserPassword("trainee2", "secure123");

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void existsByUserUsernameAndUserPassword_ShouldReturnFalse_WhenCredentialsDoNotMatch() {
    // Given
    User user = new User();
    user.setUsername("trainee3");
    user.setPassword("correct123");
    user.setFirstName("Mike");
    user.setLastName("Johnson");
    entityManager.persist(user);

    Trainee trainee = new Trainee();
    trainee.setUser(user);
    trainee.setDateOfBirth(LocalDate.of(1988, 8, 21));
    trainee.setAddress("789 Workout Blvd");
    entityManager.persist(trainee);
    entityManager.flush();

    // When
    boolean exists = traineeRepo.existsByUserUsernameAndUserPassword("trainee3", "wrong123");

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void existsByUserFirstNameAndUserLastName_ShouldReturnTrue_WhenNameExists() {
    // Given
    User user = new User();
    user.setUsername("trainee4");
    user.setPassword("pass456");
    user.setFirstName("Robert");
    user.setLastName("Brown");
    entityManager.persist(user);

    Trainee trainee = new Trainee();
    trainee.setUser(user);
    trainee.setDateOfBirth(LocalDate.of(1995, 3, 12));
    trainee.setAddress("101 Exercise Lane");
    entityManager.persist(trainee);
    entityManager.flush();

    // When
    boolean exists = traineeRepo.existsByUserFirstNameAndUserLastName("Robert", "Brown");

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void existsByUserFirstNameAndUserLastName_ShouldReturnFalse_WhenNameDoesNotExist() {
    // When
    boolean exists = traineeRepo.existsByUserFirstNameAndUserLastName("Unknown", "Person");

    // Then
    assertThat(exists).isFalse();
  }
}
