package com.epam.gymapp.microservice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record TrainerWorkload(
    @Schema(example = "SarahMaria") @NotBlank String username,
    @Schema(example = "Sarah") @NotBlank String firstName,
    @Schema(example = "Maria") @NotBlank String lastName,
    @Schema(example = "true") boolean active,
    @Schema(example = "2024-01-15T10:30:00.000Z") @NotNull Instant trainingDate,
    @Schema(example = "60") int trainingDuration) {}

