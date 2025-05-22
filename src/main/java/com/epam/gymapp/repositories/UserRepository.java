package com.epam.gymapp.repositories;

import com.epam.gymapp.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByUsername(String candidate);

  Optional<User> findByUsername(String username);

  Optional<User> findByUsernameAndPassword(String username, String password);
}
