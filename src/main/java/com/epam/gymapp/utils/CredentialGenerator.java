package com.epam.gymapp.utils;

import com.epam.gymapp.repositories.UserRepository;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CredentialGenerator {

  private final UserRepository userRepository;

  private static final String PASSWORD_CHARS =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final int DEFAULT_PASSWORD_LENGTH = 10;
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  public String randomPassword() {
    return generateRandomPassword(DEFAULT_PASSWORD_LENGTH);
  }

  public String generateRandomPassword(int length) {
    if (length <= 0) {
      throw new IllegalArgumentException("length must be positive");
    }

    StringBuilder sb = new StringBuilder(length);
    int poolSize = PASSWORD_CHARS.length();
    for (int i = 0; i < length; i++) {
      int idx = SECURE_RANDOM.nextInt(poolSize);
      sb.append(PASSWORD_CHARS.charAt(idx));
    }
    return sb.toString();
  }

  public String buildUniqueUsername(String first, String last) {
    String base = (first + "." + last).toLowerCase(Locale.ROOT);

    String candidate;
    do {
      String uuid8 = UUID.randomUUID().toString().substring(0, 8);
      candidate = base + "-" + uuid8;
    } while (userRepository.existsByUsername(candidate));

    return candidate;
  }
}
