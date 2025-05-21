package com.epam.gymapp.api.controllers;

import com.epam.gymapp.api.auth.AuthenticatedUser;
import com.epam.gymapp.api.dto.*;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.services.TraineeService;
import com.epam.gymapp.services.TrainerService;
import com.epam.gymapp.utils.ApiListWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trainee")
@RequiredArgsConstructor
@Tag(name = "Trainee")
public class TraineeController {

  private final TraineeService traineeService;
  private final TrainerService trainerService;

  /* --------- CREATE --------- */
  @PostMapping("/register")
  @Operation(summary = "Trainee Registration (1)")
  public ResponseEntity<TraineeRegistrationDto> create(@Valid @RequestBody CreateTraineeDto body) {

    TraineeRegistrationDto created = traineeService.register(body);

    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  /* ---------- READ ---------- */
  @GetMapping("/{username}")
  @Operation(summary = "Get trainee profile (5)")
  public TraineeProfileDto get(@PathVariable String username, @AuthenticatedUser User caller) {

    return traineeService.findProfile(username);
  }

  @GetMapping("/{username}/unassigned-trainers")
  @Operation(summary = "Get not assigned on trainee active trainers (10)")
  public ApiListWrapper<TrainerShortDto> unassignedActiveTrainers(
      @PathVariable String username, @AuthenticatedUser User caller) {

    return new ApiListWrapper<>(trainerService.unassignedActiveTrainers(username));
  }

  @GetMapping
  @Operation(summary = "List trainees (paged)")
  public ApiListWrapper<TraineeDto> list(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticatedUser User caller) {

    return new ApiListWrapper<>(traineeService.list(PageRequest.of(page, size)).getContent());
  }

  /* ------ UPDATE (PUT) ------ */
  @PutMapping("/{username}")
  @Operation(summary = "Update trainee profile (6)")
  public TraineeProfileDto updateProfile(
      @PathVariable String username,
      @Valid @RequestBody UpdateTraineeDto body,
      @AuthenticatedUser User caller) {

    return traineeService.updateProfile(username, body);
  }

  @PutMapping("/{username}/trainers")
  @Operation(summary = "Update Trainee's Trainer List (11)")
  public ApiListWrapper<TrainerShortDto> replaceTrainers(
      @PathVariable String username,
      @Valid @RequestBody UpdateTraineeTrainersDto body,
      @AuthenticatedUser User caller) {

    List<TrainerShortDto> out = traineeService.replaceTrainers(username, body.trainers());

    return new ApiListWrapper<>(out);
  }

  /* -------- DELETE -------- */
  @DeleteMapping("/{username}")
  @Operation(summary = "Delete trainee profile (7)")
  public ResponseEntity<Void> delete(
      @PathVariable String username, @AuthenticatedUser User caller) {

    traineeService.deleteByUsername(username);
    return ResponseEntity.ok().build();
  }

  /* -- ACTIVATE/DEACTIVATE (PATCH) -- */
  @PatchMapping("/{username}/active")
  @Operation(summary = "Activate/De-Activate Trainee (15)")
  public ResponseEntity<Void> setActive(
      @PathVariable String username, @RequestParam boolean active, @AuthenticatedUser User caller) {

    traineeService.setActive(username, active);
    return ResponseEntity.ok().build();
  }
}
