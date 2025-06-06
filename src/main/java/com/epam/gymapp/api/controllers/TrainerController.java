package com.epam.gymapp.api.controllers;

import com.epam.gymapp.api.auth.AuthenticatedUser;
import com.epam.gymapp.api.dto.*;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.services.TrainerService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
@Tag(name = "Trainer")
public class TrainerController {

  private final TrainerService trainerService;

  @Timed(
      value = "create.duration",
      description = "Time spent on creating trainer",
      histogram = true)
  @PostMapping("/register")
  @Operation(summary = "Trainer Registration (2)")
  public ResponseEntity<TrainerRegistrationDto> create(@Valid @RequestBody CreateTrainerDto dto) {

    TrainerRegistrationDto created = trainerService.register(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{username}")
  @Operation(summary = "Get trainer profile (8)")
  public TrainerProfileDto getProfile(
      @PathVariable String username, @AuthenticatedUser User caller) {

    return trainerService.findProfile(username);
  }

  @PutMapping("/{username}")
  @Operation(summary = "Update trainer profile (9)")
  public TrainerProfileDto updateProfile(
      @PathVariable String username,
      @Valid @RequestBody UpdateTrainerDto body,
      @AuthenticatedUser User caller) {

    return trainerService.updateProfile(username, body);
  }

  @PatchMapping("/{username}/active")
  @Operation(summary = "Activate/De-Activate Trainer (16)")
  public ResponseEntity<Void> setActive(
      @PathVariable String username, @RequestParam boolean active, @AuthenticatedUser User caller) {

    trainerService.setActive(username, active);
    return ResponseEntity.ok().build();
  }
}
