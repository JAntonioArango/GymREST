package com.epam.gymapp.microservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(
        name = "MICROSERVICE-TASK",  //Service name registered in Eureka
        path = "/api/workload/v1",
        fallback = WorkloadClientFallback.class
)
public interface WorkloadClient {
    @PostMapping("/saveworkload")
    TrainerWorkload save(@RequestBody TrainerWorkload body);

    @GetMapping("/summary/{username}")
    TrainerWorkloadSummary summary(@PathVariable("username") String username);

    @DeleteMapping("/delete/{id}")
    void delete(@PathVariable("id") Long id);
}
