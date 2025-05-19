package com.epam.gymapp.repositories;

import com.epam.gymapp.entities.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeRepo extends JpaRepository<Trainee, Long>{

    Optional<Trainee> findByUserUsername(String username);
    boolean existsByUserUsernameAndUserPassword(String username, String password);
    boolean existsByUserUsername(String username);
    void deleteByUserUsername(String username);
}
