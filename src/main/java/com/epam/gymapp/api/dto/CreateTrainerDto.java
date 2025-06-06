package com.epam.gymapp.api.dto;

import com.epam.gymapp.entities.Specialization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTrainerDto(
    @Schema(example = "Sara") @NotBlank String firstName,
    @Schema(example = "Maria") @NotBlank String lastName,
    @Schema(example = "BOXING") @NotNull Specialization specialization) {}
