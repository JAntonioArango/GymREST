package com.epam.gymapp.exceptions;

public class TrainingNotFoundException extends RuntimeException {

    public TrainingNotFoundException(Long trainerId) {
        super("Trainer not found with id: " + trainerId);
    }

    public TrainingNotFoundException(String name) {
        super("Trainer not found with name: " + name);
    }

}
