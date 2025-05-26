package com.epam.gymapp.api.dto;

import com.epam.gymapp.entities.Specialization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TrainingDto(
    @Schema(example = "2") @NotNull Long id,
    @Schema(example = "Daniela.Lopez123") @NotBlank String traineeUsername,
    @Schema(example = "Sara.Maria123") @NotBlank String trainerUsername,
    @Schema(example = "Sara") @NotBlank String trainerFirstName,
    @Schema(example = "Maria") @NotBlank String trainerLastName,
    @Schema(example = "RUNNING") @NotBlank Specialization trainingType,
    @Schema(example = "STRENGTH") @NotBlank String trainingName,
    @Schema(example = "1990-05-02") @NotNull LocalDate trainingDate,
    @Schema(example = "15") @NotNull Integer duration // minutes
    ) {}
