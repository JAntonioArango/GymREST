package com.epam.gymapp.credentialsTest.reposTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.UserRepo;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepoTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private UserRepo userRepo;

  @Test
  void existsByUsername_ShouldReturnTrue_WhenUsernameExists() {
    // Given
    User user = new User();
    user.setUsername("testuser");
    user.setPassword("password");
    user.setFirstName("Test");
    user.setLastName("User");
    entityManager.persist(user);
    entityManager.flush();

    // When
    boolean exists = userRepo.existsByUsername("testuser");

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void existsByUsername_ShouldReturnFalse_WhenUsernameDoesNotExist() {
    // When
    boolean exists = userRepo.existsByUsername("nonexistentuser");

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void findByUsername_ShouldReturnUser_WhenUsernameExists() {
    // Given
    User user = new User();
    user.setUsername("johndoe");
    user.setPassword("password123");
    user.setFirstName("John");
    user.setLastName("Doe");
    entityManager.persist(user);
    entityManager.flush();

    // When
    Optional<User> foundUser = userRepo.findByUsername("johndoe");

    // Then
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getUsername()).isEqualTo("johndoe");
    assertThat(foundUser.get().getFirstName()).isEqualTo("John");
    assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
  }

  @Test
  void findByUsername_ShouldReturnEmpty_WhenUsernameDoesNotExist() {
    // When
    Optional<User> foundUser = userRepo.findByUsername("nonexistentuser");

    // Then
    assertThat(foundUser).isEmpty();
  }

  @Test
  void findByUsernameAndPassword_ShouldReturnUser_WhenCredentialsMatch() {
    // Given
    User user = new User();
    user.setUsername("alice");
    user.setPassword("securepass");
    user.setFirstName("Alice");
    user.setLastName("Smith");
    entityManager.persist(user);
    entityManager.flush();

    // When
    Optional<User> foundUser = userRepo.findByUsernameAndPassword("alice", "securepass");

    // Then
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getUsername()).isEqualTo("alice");
    assertThat(foundUser.get().getPassword()).isEqualTo("securepass");
  }

  @Test
  void findByUsernameAndPassword_ShouldReturnEmpty_WhenCredentialsDoNotMatch() {
    // Given
    User user = new User();
    user.setUsername("bob");
    user.setPassword("correctpass");
    user.setFirstName("Bob");
    user.setLastName("Brown");
    entityManager.persist(user);
    entityManager.flush();

    // When
    Optional<User> foundUser = userRepo.findByUsernameAndPassword("bob", "wrongpass");

    // Then
    assertThat(foundUser).isEmpty();
  }
}
