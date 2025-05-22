package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TraineeShortDto(
    @Schema(example = "Daniela.Lopez123") String username,
    @Schema(example = "Daniela") String firstName,
    @Schema(example = "Lopez") String lastName) {}
