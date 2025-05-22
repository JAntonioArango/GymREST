package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record TraineeTrainingDto(
    @Schema(example = "STRENGTH") String trainingName,
    @Schema(example = "2025-05-02") LocalDate trainingDate,
    @Schema(example = "RUNNING") String trainingType,
    @Schema(example = "15") Integer durationMinutes,
    @Schema(example = "Sara") String trainerName) {}
