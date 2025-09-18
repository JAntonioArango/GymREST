package com.epam.gymapp.microservice;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainerWorkloadSummary {
  private String username;
  private String firstName;
  private String lastName;
  private boolean isActive;
  private List<Integer> years;
  private List<String> months;
  private int totalDuration;
}
