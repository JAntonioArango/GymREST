package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TrainingTypeDto(
    @Schema(example = "2") Long id, @Schema(example = "STRENGTH") String name) {}
