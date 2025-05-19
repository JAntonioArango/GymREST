package com.epam.gymapp.bootstrap;

import com.epam.gymapp.entities.TrainingType;
import com.epam.gymapp.repositories.TrainingTypeRepo;
import org.springframework.boot.CommandLineRunner;

public class TrainingTypeSeeder implements CommandLineRunner {

    private final TrainingTypeRepo repo;

    public TrainingTypeSeeder(TrainingTypeRepo repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        String[] defaults = {
                "AEROBICS", "CARDIO", "STRENGTH", "YOGA",
                "PILATES",  "CROSSFIT", "SPINNING", "ZUMBA",
                "BODYBUILDING", "BALANCE", "DANCING",
                "BOXING", "MEDITATION", "OTHER"
        };

        for (String name : defaults) {
            if (!repo.existsByName(name)) {
                TrainingType tt = new TrainingType();
                tt.setName(name);
                repo.save(tt);
            }
        }
    }
}
