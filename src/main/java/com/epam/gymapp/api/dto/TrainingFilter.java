package com.epam.gymapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TrainingFilter(
        @NotNull LocalDate fromDate,
        @NotNull LocalDate toDate,
        @NotBlank String trainerName,
        @NotBlank String trainingType
)
{ }