package com.epam.gymapp.exceptions;

public class TraineeNotFoundException extends RuntimeException {

    public TraineeNotFoundException(Long traineeId) {
        super("Trainee not found with id: " + traineeId);
    }

    public TraineeNotFoundException(String name) {
        super("Trainee not found with name: " + name);
    }

}

