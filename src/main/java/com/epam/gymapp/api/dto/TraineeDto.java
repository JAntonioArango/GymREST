package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TraineeDto(
    @Schema(example = "Daniela.Lopez123") @NotBlank String username,
    @Schema(example = "Daniela") @NotBlank String firstName,
    @Schema(example = "Lopez") @NotBlank String lastName,
    @Schema(example = "1990-05-02") @NotNull LocalDate dateOfBirth,
    @Schema(example = "123 Main St") @NotBlank String address,
    @Schema(example = "true") @NotNull boolean active) {}
