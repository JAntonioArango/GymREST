package com.epam.gymapp.services;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final AuthenticationManager authManager;
  private final PasswordEncoder encoder;
  private final UserRepository userRepo;
  private final LoginAttemptService attemptService;

  public User validate(String username, String rawPassword) {

    attemptService.assertNotLocked(username);

    try {
      Authentication auth =
          authManager.authenticate(new UsernamePasswordAuthenticationToken(username, rawPassword));

      SecurityContextHolder.getContext().setAuthentication(auth);
      attemptService.reset(username);
      return userRepo.findByUsername(username).orElseThrow();
    } catch (BadCredentialsException e) {
      attemptService.recordFailure(username);
      throw ApiException.badCredentials();
    }
  }

  @Transactional
  public void changePassword(String username, String oldPassword, String newPassword) {

    User user =
        userRepo
            .findByUsername(username)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

    user.setPassword(encoder.encode(newPassword));
  }
}
