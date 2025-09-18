package com.epam.gymapp.microservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkloadGateway {

  private final WorkloadClient client;

  public TrainerWorkload save(TrainerWorkload workload) {

    return client.save(workload);
  }

  public TrainerWorkloadSummary summary(String username) {

    return client.summary(username);
  }

  public void delete(Long id) {

    client.delete(id);
  }
}
