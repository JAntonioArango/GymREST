package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

public record TraineeProfileDto(
    @Schema(example = "Daniela.Lopez123") String userName,
    @Schema(example = "Daniela") String firstName,
    @Schema(example = "Lopez") String lastName,
    @Schema(example = "1990-05-02") LocalDate dateOfBirth,
    @Schema(example = "123 Main St") String address,
    @Schema(example = "true") boolean isActive,
    @Schema(example = "[]") List<TrainerShortDto> trainers) {}
