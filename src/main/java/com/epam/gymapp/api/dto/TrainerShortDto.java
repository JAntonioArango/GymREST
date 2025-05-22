package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TrainerShortDto(
    @Schema(example = "Sara.Maria123") String username,
    @Schema(example = "Sara") String firstName,
    @Schema(example = "Maria") String lastName,
    @Schema(example = "BOXING") String specialization) {}
