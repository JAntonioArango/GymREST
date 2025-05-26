package com.epam.gymapp.api.dto;

import com.epam.gymapp.entities.Specialization;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record TrainerTrainingDto(
    @Schema(example = "STRENGTH") String trainingName,
    @Schema(example = "2025-05-02") LocalDate trainingDate,
    @Schema(example = "RUNNING") Specialization trainingType,
    @Schema(example = "15") Integer durationMinutes,
    @Schema(example = "Daniela") String traineeName) {}
