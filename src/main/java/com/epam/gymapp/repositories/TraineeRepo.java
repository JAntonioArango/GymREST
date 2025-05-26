package com.epam.gymapp.repositories;

import com.epam.gymapp.entities.Trainee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepo extends JpaRepository<Trainee, Long> {

  Optional<Trainee> findByUserUsername(String username);

  boolean existsByUserFirstNameAndUserLastName(String firstName, String lastName);
}
