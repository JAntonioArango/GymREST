package com.epam.gymapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTrainerDto(
    @NotBlank String firstName,
    @NotBlank String lastName,
    String specialization,
    @NotNull Boolean isActive) {}
