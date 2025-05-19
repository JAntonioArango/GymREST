package com.epam.gymapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateTrainingDto(
        @NotBlank String traineeUsername,
        @NotBlank String trainerUsername,
        @NotBlank String trainingTypeCode,
        @NotBlank String name,
        @NotNull LocalDate date,
        @NotNull Integer duration               // minutes
) {}
