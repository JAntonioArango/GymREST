package com.epam.gymapp.exceptions;

public class TrainerNotFoundException extends RuntimeException {

    public TrainerNotFoundException(Long trainerId) {
        super("Trainer not found with id: " + trainerId);
    }

    public TrainerNotFoundException(String name) {
        super("Trainer not found with name: " + name);
    }

}
