package com.epam.gymapp.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {

  @Value("${jwt.secret}")
  private String secretB64;

  @Getter
  @Value("${jwt.expiration}")
  private int expiresMinutes;

  @PostConstruct
  void validate() {
    if (secretB64 == null || secretB64.isBlank()) {
      throw new IllegalStateException("Missing JWT_SECRET environment variable");
    }

    try {
      Base64.getDecoder().decode(secretB64);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("JWT_SECRET is not valid base64", e);
    }
  }

  private SecretKey secretKey() {
    byte[] keyBytes = Base64.getDecoder().decode(secretB64);
    return new SecretKeySpec(keyBytes, "HmacSHA256");
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withSecretKey(secretKey()).build();
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey()));
  }
}
