package com.epam.gymapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record CreateTrainingDto(
    @NotBlank String traineeUsername,
    @NotBlank String trainerUsername,
    @NotBlank String trainingName,
    @NotNull LocalDate date,
    @NotNull @Positive Integer duration // minutes
    ) {}
