package com.epam.gymapp.repositories;

import com.epam.gymapp.entities.TrainingType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepo extends JpaRepository<TrainingType, Long> {

  boolean existsByName(String name);

  Optional<TrainingType> findByName(String name);
}
