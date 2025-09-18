package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateTraineeDto(
        @Schema(example = "SaraTe") @NotBlank String username,
    @Schema(example = "Sara") @NotBlank String firstName,
    @Schema(example = "Maria") @NotBlank String lastName,
    @Schema(example = "2025-05-02") LocalDate dateOfBirth,
    @Schema(example = "123 Main St") String address,
    @Schema(example = "true") @NotNull Boolean isActive) {}
