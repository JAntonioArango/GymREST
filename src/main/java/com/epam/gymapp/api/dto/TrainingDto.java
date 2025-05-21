package com.epam.gymapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TrainingDto(
    @NotNull Long id,
    @NotBlank String traineeUsername,
    @NotBlank String trainerUsername,
    @NotBlank String trainerFirstName,
    @NotBlank String trainerLastName,
    @NotBlank String trainingType,
    @NotBlank String trainingName,
    @NotNull LocalDate trainingDate,
    @NotNull Integer duration // minutes
    ) {}
