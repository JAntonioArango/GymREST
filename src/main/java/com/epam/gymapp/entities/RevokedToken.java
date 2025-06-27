package com.epam.gymapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "revoked_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevokedToken {
  @Id
  @Column(length = 64, nullable = false)
  private String token;

  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;
}
