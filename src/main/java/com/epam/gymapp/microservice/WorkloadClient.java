package com.epam.gymapp.microservice;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "MICROSERVICE-TASK", // Service name registered in Eureka
    path = "/api/workload/v1",
    fallback = WorkloadClientFallback.class)
public interface WorkloadClient {
  @PostMapping("/saveworkload")
  @CircuitBreaker(name = "MICROSERVICE-TASK")
  TrainerWorkload save(@RequestBody TrainerWorkload body);

  @GetMapping("/summary/{username}")
  @CircuitBreaker(name = "MICROSERVICE-TASK")
  TrainerWorkloadSummary summary(@PathVariable("username") String username);

  @DeleteMapping("/delete/{id}")
  @CircuitBreaker(name = "MICROSERVICE-TASK")
  void delete(@PathVariable("id") Long id);
}
