package com.epam.gymapp.utils;

import com.epam.gymapp.repositories.UserRepository;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CredentialGenerator {

  private final UserRepository userRepository;

  public String randomPassword() {
    return RandomStringUtils.randomAlphanumeric(10);
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
