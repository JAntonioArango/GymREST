package com.epam.gymapp.api.controllers;

import com.epam.gymapp.api.dto.*;
import com.epam.gymapp.services.TrainingService;
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

  @PostMapping("/add")
  @Operation(summary = "Add Training (14)")
  public ResponseEntity<Void> add(@Valid @RequestBody CreateTrainingDto body) {

    service.addTraining(body);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/trainee/{username}")
  @Operation(summary = "Get Trainee Trainings List (12)")
  public ResponseEntity<List<TraineeTrainingDto>> traineeTrainings(
      @PathVariable String username,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(required = false) String trainerName,
      @RequestParam(required = false) String trainingType,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    var filter = new TrainingService.TrainingFilter(from, to, trainerName, null, trainingType);

    Page<TraineeTrainingDto> p =
        service.traineeTrainings(username, filter, PageRequest.of(page, size));

    return ResponseEntity.ok(p.getContent());
  }

  @GetMapping("/trainer/{username}")
  @Operation(summary = "Get Trainer Trainings List (13)")
  public ResponseEntity<List<TrainerTrainingDto>> trainerTrainings(
      @PathVariable String username,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(required = false) String traineeName,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    var filter = new TrainingService.TrainingFilter(from, to, null, traineeName, null);

    Page<TrainerTrainingDto> p =
        service.trainerTrainings(username, filter, PageRequest.of(page, size));

    return ResponseEntity.ok(p.getContent());
  }

  @GetMapping("/types")
  @Operation(summary = "Get Training Types (17)")
  public ResponseEntity<List<TrainingTypeDto>> listTypes() {

    List<TrainingTypeDto> types = service.listTrainingTypes();

    return ResponseEntity.ok(types);
  }
}
