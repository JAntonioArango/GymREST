package com.epam.gymapp.microservice;

import com.epam.gymapp.activemq.ProducerController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/micro/v1")
@RequiredArgsConstructor
@Slf4j
public class WorkloadProxyController {

  private final WorkloadGateway gateway;
  private final ProducerController producerController;

  @PostMapping("/saveworkload")
  public ResponseEntity<String> save(@RequestBody TrainerWorkload workLoad) {
    String queue = "Asynchronous.Task";
    producerController.send(queue, workLoad.toString());
    log.info("Workload queued for processing: {}", workLoad.username());
    return ResponseEntity.accepted().body("Workload queued for processing");
  }

  @GetMapping("/summary/{username}")
  public ResponseEntity<TrainerWorkloadSummary> getSummary(
      @PathVariable("username") String username) {
    return ResponseEntity.ok(gateway.summary(username));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    gateway.delete(id);
    return ResponseEntity.noContent().build();
  }
}
