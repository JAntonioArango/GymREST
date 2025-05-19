package com.epam.gymapp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private boolean isActive = true;

    @OneToOne(mappedBy = "user")
    private Trainer trainer;

    @OneToOne(mappedBy = "user")
    private Trainee trainee;
}
