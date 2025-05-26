package com.epam.gymapp.repositories;

import com.epam.gymapp.entities.Specialization;
import com.epam.gymapp.entities.TrainingType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepo extends JpaRepository<TrainingType, Long> {

  Optional<TrainingType> findByName(Specialization name);
}
