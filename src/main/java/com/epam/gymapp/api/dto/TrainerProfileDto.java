package com.epam.gymapp.api.dto;

import java.util.List;

public record TrainerProfileDto(
        String username,
        String firstName,
        String lastName,
        String specialization,
        boolean isActive,
        List<TraineeShortDto> trainees)
{ }
