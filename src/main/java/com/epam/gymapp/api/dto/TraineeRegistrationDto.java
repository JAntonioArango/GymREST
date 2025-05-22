package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TraineeRegistrationDto(
    @Schema(example = "Daniela.Lopez123") String username,
    @Schema(example = "Pass123") String password) {}
