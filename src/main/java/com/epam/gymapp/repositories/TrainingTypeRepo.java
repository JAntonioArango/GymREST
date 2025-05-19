package com.epam.gymapp.repositories;

import com.epam.gymapp.entities.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepo extends JpaRepository<TrainingType, Long> {

    boolean existsByName(String name);

    Optional<TrainingType> findByName(String name);
}
