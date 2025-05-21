package com.epam.gymapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TrainerDto(
        @NotNull Long id,
        @NotBlank String username,
        @NotNull boolean active,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String specialization
)
{ }
