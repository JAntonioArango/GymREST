package com.epam.gymapp.repositories;

import com.epam.gymapp.entities.Trainer;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepo extends JpaRepository<Trainer, Long> {

  Optional<Trainer> findByUserUsername(String username);

  boolean existsByUserUsernameAndUserPassword(String username, String password);

  List<Trainer> findByTraineesUserUsername(String username);

  @NotNull
  List<Trainer> findAll();

  List<Trainer> findByUserUsernameIn(List<String> usernames);
}
