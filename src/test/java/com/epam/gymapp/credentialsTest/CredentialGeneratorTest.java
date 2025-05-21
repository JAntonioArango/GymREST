package com.epam.gymapp.credentialsTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.epam.gymapp.repositories.UserRepo;
import com.epam.gymapp.utils.CredentialGenerator;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CredentialGeneratorTest {

  @Mock private UserRepo userRepo;

  private CredentialGenerator credentialGenerator;

  @BeforeEach
  void setUp() {
    credentialGenerator = new CredentialGenerator(userRepo);
  }

  @Test
  void randomPassword_ShouldReturnAlphanumericPasswordOfLength10() {
    // When
    String password = credentialGenerator.randomPassword();

    // Then
    assertNotNull(password);
    assertEquals(10, password.length());
    assertTrue(password.matches("[a-zA-Z0-9]+"));
  }

  @Test
  void randomPassword_ShouldGenerateDifferentPasswordsOnMultipleCalls() {
    // When
    String password1 = credentialGenerator.randomPassword();
    String password2 = credentialGenerator.randomPassword();

    // Then
    assertNotEquals(password1, password2);
  }

  @Test
  void buildUniqueUsername_ShouldCreateUsernameWithFirstLastNameAndUuid() {
    // Given
    when(userRepo.existsByUsername(anyString())).thenReturn(false);

    // When
    String username = credentialGenerator.buildUniqueUsername("John", "Doe");

    // Then
    assertNotNull(username);
    assertTrue(username.startsWith("john.doe-"));

    // Extract UUID portion
    String uuidPart = username.substring("john.doe-".length());
    assertEquals(8, uuidPart.length());

    // Verify userRepo was called
    verify(userRepo).existsByUsername(username);
  }

  @Test
  void buildUniqueUsername_ShouldRetryWhenUsernameExists() {
    // Given
    // First attempt finds existing username, second attempt succeeds
    when(userRepo.existsByUsername(anyString())).thenReturn(true).thenReturn(false);

    // When
    String username = credentialGenerator.buildUniqueUsername("Alice", "Smith");

    // Then
    assertNotNull(username);
    assertTrue(username.startsWith("alice.smith-"));

    // Verify userRepo was called twice
    verify(userRepo, times(2)).existsByUsername(anyString());
  }

  @Test
  void buildUniqueUsername_ShouldHandleSpecialCharactersInNames() {
    // Given
    when(userRepo.existsByUsername(anyString())).thenReturn(false);

    // When
    String username = credentialGenerator.buildUniqueUsername("Joé", "O'Hara");

    // Then
    assertNotNull(username);
    assertTrue(username.startsWith("joé.o'hara-"));

    // UUID format still maintained
    Pattern uuidPattern = Pattern.compile("joé\\.o'hara-[a-f0-9]{8}");
    assertTrue(uuidPattern.matcher(username).matches());
  }
}
