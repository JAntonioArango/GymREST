package com.epam.gymapp.microservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/micro/v1")
@RequiredArgsConstructor
public class WorkloadProxyController {
    private final WorkloadGateway gateway;

    @PostMapping("/saveworkload")
    public ResponseEntity<TrainerWorkload> save(@RequestBody TrainerWorkload workLoad){
        return ResponseEntity.ok(gateway.save(workLoad));
    }

    @GetMapping("/summary/{username}")
    public ResponseEntity<TrainerWorkloadSummary> getSummary(@PathVariable("username") String username){
        return ResponseEntity.ok(gateway.summary(username));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        gateway.delete(id);
        return ResponseEntity.noContent().build();
    }

}
