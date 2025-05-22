package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TrainingFilter(
    @Schema(example = "2025-05-02") @NotNull LocalDate fromDate,
    @Schema(example = "2025-05-10") @NotNull LocalDate toDate,
    @Schema(example = "Sara") @NotBlank String trainerName,
    @Schema(example = "true") @NotBlank String trainingType) {}
