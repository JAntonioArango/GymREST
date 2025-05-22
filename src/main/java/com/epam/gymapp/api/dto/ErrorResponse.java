package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error payload in RFC-7807 / Problem-Detail format")
public record ErrorResponse(
    @Schema(example = "404") int status,
    @Schema(example = "Resource not found") String title,
    @Schema(example = "Trainee not found: john.doe") String detail,
    @Schema(example = "/api/v1/trainee/john.doe") String path) {}
