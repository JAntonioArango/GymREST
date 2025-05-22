package com.epam.gymapp.services;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repo;

  @Transactional(readOnly = true)
  public User validate(String username, String password) {

    return repo.findByUsernameAndPassword(username, password)
        .orElseThrow(() -> ApiException.badCredentials());
  }

  @Transactional
  public void changePassword(String username, String oldPassword, String newPassword) {

    User user = validate(username, oldPassword);
    user.setPassword(newPassword);
  }
}
