package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TrainerRegistrationDto(
    @Schema(example = "Sara.Maria123") String username,
    @Schema(example = "Pass123") String password) {}
