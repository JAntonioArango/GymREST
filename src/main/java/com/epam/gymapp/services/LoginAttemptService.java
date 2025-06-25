package com.epam.gymapp.services;

import com.epam.gymapp.api.advice.ApiException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

  private static final int MAX_ATTEMPTS = 3;
  private static final int BLOCKED_MINUTES = 5;

  private final Cache<String, Integer> attempts =
      Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(BLOCKED_MINUTES)).build();

  public void assertNotLocked(String username) {
    Integer n = attempts.getIfPresent(username);
    if (n != null && n >= MAX_ATTEMPTS) {
      throw ApiException.badRequest("Account locked for 5 minutes");
    }
  }

  public void recordFailure(String username) {
    int newCount = attempts.asMap().merge(username, 1, Integer::sum);
    if (newCount >= MAX_ATTEMPTS) {
      throw new LockedException("Account locked for 5 minutes");
    }
  }

  public void reset(String username) {
    attempts.invalidate(username);
  }
}
