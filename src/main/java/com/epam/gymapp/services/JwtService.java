package com.epam.gymapp.services;

import com.epam.gymapp.config.JwtConfig;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

  private final JwtEncoder encoder;
  private final JwtDecoder decoder;
  private final JwtConfig jwtConfig;

  public String createToken(String username) {
    Instant now = Instant.now();
    Instant expiresAt = now.plus(Duration.ofMinutes(jwtConfig.getExpiresMinutes()));
    
    log.debug("JWT created at: {}, expires at: {}", now, expiresAt);

    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .subject(username)
            .issuedAt(now)
            .expiresAt(expiresAt)
            .issuer("gym-task")
            .claim("username", username)
            .build();

    JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

    return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
  }

  public Jwt parse(String token) {
    return decoder.decode(token);
  }
}
