package com.epam.gymapp.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "trainings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Training {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "trainee_id")
  private Trainee trainee;

  @ManyToOne(optional = false)
  @JoinColumn(name = "trainer_id")
  private Trainer trainer;

  @ManyToOne(optional = false)
  @JoinColumn(name = "training_type_id")
  private TrainingType trainingType;

  @Column private String trainingName;

  @Column
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDate trainingDate;

  @Column private Integer trainingDuration;

  @Column private Boolean activeSession;
}
