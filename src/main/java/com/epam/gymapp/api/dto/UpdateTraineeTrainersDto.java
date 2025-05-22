package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record UpdateTraineeTrainersDto(@Schema(example = "[]") @NotEmpty List<String> trainers) {}
