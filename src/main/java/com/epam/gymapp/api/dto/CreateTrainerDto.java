package com.epam.gymapp.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTrainerDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String specialization
)
{ }
