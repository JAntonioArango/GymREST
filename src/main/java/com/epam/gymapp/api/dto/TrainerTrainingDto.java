package com.epam.gymapp.api.dto;

import java.time.LocalDate;

public record TrainerTrainingDto(
    String trainingName,
    LocalDate trainingDate,
    String trainingType,
    Integer durationMinutes,
    String traineeName) {}
