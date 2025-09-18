package com.epam.gymapp.api.dto;

import com.epam.gymapp.entities.Specialization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTrainerDto(
    @Schema(example = "SaraTr") @NotBlank String username,
    @Schema(example = "Sara") @NotBlank String firstName,
    @Schema(example = "Maria") @NotBlank String lastName,
    @Schema(example = "BOXING") Specialization specialization,
    @Schema(example = "true") @NotNull Boolean isActive) {}
