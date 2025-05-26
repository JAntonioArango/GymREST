package com.epam.gymapp.api.dto;

import com.epam.gymapp.entities.Specialization;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record TrainerProfileDto(
    @Schema(example = "Sara.Maria123") String username,
    @Schema(example = "Sara") String firstName,
    @Schema(example = "Maria") String lastName,
    @Schema(example = "BOXING") Specialization specialization,
    @Schema(example = "true") boolean isActive,
    @Schema(example = "[]") List<TraineeShortDto> trainees) {}
