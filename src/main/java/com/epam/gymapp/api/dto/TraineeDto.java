package com.epam.gymapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TraineeDto(
        @NotBlank String username,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull LocalDate dateOfBirth,
        @NotBlank String address,
        @NotNull boolean active
)
{ }

