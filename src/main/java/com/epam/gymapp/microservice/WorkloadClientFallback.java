package com.epam.gymapp.microservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
public class WorkloadClientFallback implements WorkloadClient {
    
    @Override
    public TrainerWorkload save(TrainerWorkload body) {
        log.warn("Workload service is unavailable, returning fallback response for: {}", body.username());
        return new TrainerWorkload(
            body.username(),
            body.firstName(), 
            body.lastName(),
            body.active(),
            Instant.now(),
            body.trainingDuration()
        );
    }

    @Override
    public TrainerWorkloadSummary summary(String username) {
        log.warn("Workload service is unavailable, returning fallback response for: {}", username);
        return new TrainerWorkloadSummary(
            username,
            "Fallback",
            "Fallback",
            true,
            List.of(2025),
            List.of("January"),
            0
        );
    }

    @Override
    public void delete(Long id) {
        log.warn("Workload service is unavailable, returning fallback response for: {}", id);
    }
}