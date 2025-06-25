package com.epam.gymapp.services;

import com.epam.gymapp.config.JwtConfig;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtEncoder encoder;
  private final JwtConfig jwtConfig;

  public String createToken(String username) {
    Instant now = Instant.now();

    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .subject(username)
            .issuedAt(now)
            .expiresAt(now.plus(Duration.ofMinutes(jwtConfig.getExpiresMinutes())))
            .issuer("gym-task")
            .claim("username", username)
            .build();

    JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

    return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
  }
}
