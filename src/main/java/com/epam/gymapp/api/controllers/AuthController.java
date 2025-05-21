package com.epam.gymapp.api.controllers;

import com.epam.gymapp.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

  private final AuthenticationService authService;

  /* --------- LOGIN --------- */
  @GetMapping("/login")
  @Operation(summary = "Login (3)")
  public ResponseEntity<Void> login(@RequestParam String username, @RequestParam String password) {

    authService.validate(username, password);

    return ResponseEntity.ok().build();
  }

  /* ----- CHANGE PASSWORD ----- */
  @PutMapping("/change-password")
  @Operation(summary = "Change Password (4)")
  public ResponseEntity<Void> changePassword(
      @RequestParam String username,
      @RequestParam String oldPassword,
      @RequestParam String newPassword) {

    authService.changePassword(username, oldPassword, newPassword);

    return ResponseEntity.ok().build();
  }
}
