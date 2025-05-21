package com.epam.gymapp.api.dto;

import java.time.LocalDate;
import java.util.List;

public record TraineeProfileDto(
    String userName,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String address,
    boolean isActive,
    List<TrainerShortDto> trainers) {}
