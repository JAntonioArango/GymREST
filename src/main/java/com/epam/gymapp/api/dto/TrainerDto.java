package com.epam.gymapp.api.dto;

import com.epam.gymapp.entities.Specialization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TrainerDto(
    @Schema(example = "1") @NotNull Long id,
    @Schema(example = "Sara.Maria123") @NotBlank String username,
    @Schema(example = "true") @NotNull boolean active,
    @Schema(example = "Sara") @NotBlank String firstName,
    @Schema(example = "Maria") @NotBlank String lastName,
    @Schema(example = "BOXING") @NotBlank Specialization specialization) {}
