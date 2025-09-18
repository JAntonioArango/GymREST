package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record CreateTrainingDto(
    @Schema(example = "Daniela.Lopez123") @NotBlank String traineeUsername,
    @Schema(example = "Sara.Maria123") @NotBlank String trainerUsername,
    @Schema(example = "STRENGTH") @NotBlank String trainingName,
    @Schema(example = "1990-05-02") @NotNull LocalDate date,
    @Schema(example = "15") @NotNull @Positive Integer duration, // minutes
    @Schema(example = "true") boolean activeSession) {}
