package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateTraineeDto(
    @Schema(example = "Daniela") @NotBlank String firstName,
    @Schema(example = "Lopez") @NotBlank String lastName,
    @Schema(example = "1990-05-02") LocalDate dateOfBirth,
    @Schema(example = "123 Main St") String address) {}
