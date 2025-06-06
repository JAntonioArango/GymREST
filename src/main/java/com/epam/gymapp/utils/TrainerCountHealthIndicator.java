package com.epam.gymapp.utils;

import com.epam.gymapp.repositories.TrainerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("trainerCount")
@RequiredArgsConstructor
public class TrainerCountHealthIndicator implements HealthIndicator {

  private final TrainerRepo trainerRepo;

  @Override
  public Health health() {
    long total = trainerRepo.count();
    if (total == 0) {
      return Health.down()
          .withDetail("activeTrainers", total)
          .withDetail("message", "No trainers in database")
          .build();
    }
    return Health.up().withDetail("activeTrainers", total).build();
  }
}
