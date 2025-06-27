package com.epam.gymapp.api.advice;

import com.epam.gymapp.entities.RevokedToken;
import com.epam.gymapp.repositories.RevokedTokenRepo;
import com.epam.gymapp.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

  private final JwtService jwtService;
  private final RevokedTokenRepo repo;

  @Override
  @Transactional
  public void logout(HttpServletRequest req, HttpServletResponse res, Authentication auth) {

    String hdr = req.getHeader(HttpHeaders.AUTHORIZATION);
    if (hdr == null || !hdr.startsWith("Bearer ")) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String token = hdr.substring(7);
    Jwt decoded = jwtService.parse(token);
    Instant exp = decoded.getExpiresAt();

    repo.save(new RevokedToken(token, exp));

    log.debug("RevokedToken saved: {}", token);
  }
}
