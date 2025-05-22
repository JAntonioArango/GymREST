package com.epam.gymapp.entitiesTest;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gymapp.entities.Trainee;
import com.epam.gymapp.entities.Trainer;
import com.epam.gymapp.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserEntityTest {
  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
  }

  @Test
  void testNoArgsConstructor() {
    assertNotNull(user);
    assertNull(user.getId());
    assertNull(user.getFirstName());
    assertNull(user.getLastName());
    assertNull(user.getUsername());
    assertNull(user.getPassword());
    assertTrue(user.isActive());
    assertNull(user.getTrainer());
    assertNull(user.getTrainee());
  }

  @Test
  void testAllArgsConstructor() {
    Trainer trainer = new Trainer();
    Trainee trainee = new Trainee();

    User user = new User(1L, "John", "Doe", "johndoe", "password123", true, trainer, trainee);

    assertEquals(1L, user.getId());
    assertEquals("John", user.getFirstName());
    assertEquals("Doe", user.getLastName());
    assertEquals("johndoe", user.getUsername());
    assertEquals("password123", user.getPassword());
    assertTrue(user.isActive());
    assertEquals(trainer, user.getTrainer());
    assertEquals(trainee, user.getTrainee());
  }

  @Test
  void testSetAndGetId() {
    user.setId(1L);
    assertEquals(1L, user.getId());
  }

  @Test
  void testSetAndGetFirstName() {
    user.setFirstName("John");
    assertEquals("John", user.getFirstName());
  }

  @Test
  void testSetAndGetLastName() {
    user.setLastName("Doe");
    assertEquals("Doe", user.getLastName());
  }

  @Test
  void testSetAndGetUsername() {
    user.setUsername("johndoe");
    assertEquals("johndoe", user.getUsername());
  }

  @Test
  void testSetAndGetPassword() {
    user.setPassword("password123");
    assertEquals("password123", user.getPassword());
  }

  @Test
  void testSetAndGetIsActive() {
    user.setActive(false);
    assertFalse(user.isActive());

    user.setActive(true);
    assertTrue(user.isActive());
  }

  @Test
  void testDefaultIsActiveValue() {
    User newUser = new User();
    assertTrue(newUser.isActive());
  }

  @Test
  void testSetAndGetTrainer() {
    Trainer trainer = new Trainer();
    user.setTrainer(trainer);
    assertEquals(trainer, user.getTrainer());
  }

  @Test
  void testSetAndGetTrainee() {
    Trainee trainee = new Trainee();
    user.setTrainee(trainee);
    assertEquals(trainee, user.getTrainee());
  }

  @Test
  void testUniqueUsernameConstraint() {
    User user1 = new User();
    user1.setUsername("uniqueusername");

    User user2 = new User();
    user2.setUsername("uniqueusername");

    // Note: Actual database constraints would need to be tested with integration tests
    assertNotEquals(user1, user2);
  }

  @Test
  void testUserWithNoTrainerOrTrainee() {
    User user = new User();
    assertNull(user.getTrainer());
    assertNull(user.getTrainee());
  }
}
