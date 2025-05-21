package com.epam.gymapp.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "trainers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "specialization_id")
  private TrainingType specialization;

  @OneToOne(optional = false, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", unique = true)
  private User user;

  @ManyToMany(mappedBy = "trainers")
  private Set<Trainee> trainees = new HashSet<>();

  @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Training> trainings = new HashSet<>();
}
