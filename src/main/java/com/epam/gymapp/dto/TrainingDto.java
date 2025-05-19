package com.epam.gymapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TrainingDto(
        @NotNull Long id,
        @NotBlank String traineeUsername,
        @NotBlank String trainerUsername,
        @NotBlank String trainingType,
        @NotBlank String name,
        @NotNull LocalDate date,
        @NotNull Integer duration               // minutes
) {}