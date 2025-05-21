package com.epam.gymapp.api.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UpdateTraineeTrainersDto(
        @NotEmpty List<String> trainers
) {
}
