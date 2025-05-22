package com.epam.gymapp.api.controllers;

import com.epam.gymapp.api.auth.AuthenticatedUser;
import com.epam.gymapp.api.dto.*;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.services.TrainingService;
import com.epam.gymapp.utils.ApiListWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/training")
@RequiredArgsConstructor
@Tag(name = "Training")
public class TrainingController {

  private final TrainingService service;

  /* --------- CREATE --------- */
  @PostMapping("/add")
  @Operation(summary = "Add Training (14)")
  public ResponseEntity<Void> add(
      @Valid @RequestBody CreateTrainingDto body, @AuthenticatedUser User caller) {

    service.addTraining(body);

    return ResponseEntity.ok().build();
  }

  /* --------- READ --------- */

  @GetMapping("/trainee/{username}")
  @Operation(summary = "Get Trainee Trainings List (12)")
  public ApiListWrapper<TraineeTrainingDto> traineeTrainings(
      @PathVariable String username,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(required = false) String trainerName,
      @RequestParam(required = false) String trainingType,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticatedUser User caller) {

    var filter = new TrainingService.TrainingFilter(from, to, trainerName, null, trainingType);

    Page<TraineeTrainingDto> p =
        service.traineeTrainings(username, filter, PageRequest.of(page, size));

    return new ApiListWrapper<>(p.getContent());
  }

  @GetMapping("/trainer/{username}")
  @Operation(summary = "Get Trainer Trainings List (13)")
  public ApiListWrapper<TrainerTrainingDto> trainerTrainings(
      @PathVariable String username,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(required = false) String traineeName,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticatedUser User caller) {

    var filter = new TrainingService.TrainingFilter(from, to, null, traineeName, null);

    Page<TrainerTrainingDto> p =
        service.trainerTrainings(username, filter, PageRequest.of(page, size));

    return new ApiListWrapper<>(p.getContent());
  }

  @GetMapping("/types")
  @Operation(summary = "Get Training Types (17)")
  public ApiListWrapper<TrainingTypeDto> listTypes(@AuthenticatedUser User caller) {

    List<TrainingTypeDto> types = service.listTrainingTypes();

    return new ApiListWrapper<>(types);
  }

  /* ------ HELPERS ------ */
  //  private static boolean isDateOk(TrainingDto t, TrainingService.TrainingFilter f) {
  //    boolean dateOk =
  //        (f.fromDate() == null || !t.trainingDate().isBefore(f.fromDate()))
  //            && (f.toDate() == null || !t.trainingDate().isAfter(f.toDate()));
  //    return dateOk;
  //  }
}
