package com.epam.gymapp.microservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WorkloadClientFallbackFactory implements FallbackFactory<WorkloadClient> {
    @Override
    public WorkloadClient create(Throwable cause) {
        return new WorkloadClient() {
            @Override
            public TrainerWorkload save(TrainerWorkload body) {
                log.warn("Fallback (save): {}", cause.toString());
                // return a sensible fallback...
                return new TrainerWorkload(
                        body.username(),
                        body.firstName(),
                        body.lastName(),
                        body.active(),
                        java.time.Instant.now(),
                        body.trainingDuration()
                );
            }

            @Override
            public TrainerWorkloadSummary summary(String username) {
                log.warn("Fallback (summary): {}", cause.toString());
                return new TrainerWorkloadSummary(
                        username, "Fallback", "Fallback", true,
                        java.util.List.of(2025),
                        java.util.List.of("January"),
                        0
                );
            }

            @Override
            public void delete(Long id) {
                log.warn("Fallback (delete): {}", cause.toString());
            }
        };
    }
}