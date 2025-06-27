package com.epam.gymapp.repositories;

import com.epam.gymapp.entities.RevokedToken;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokenRepo extends JpaRepository<RevokedToken, String> {
  void deleteAllByExpiresAtBefore(Instant now);
}
