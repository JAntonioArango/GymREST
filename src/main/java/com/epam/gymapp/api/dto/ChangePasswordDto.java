package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDto(
    @Schema(example = "Pass123") @NotBlank String oldPassword,
    @Schema(example = "N3wP@ssw0rd!") @NotBlank String newPassword) {}
