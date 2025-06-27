package com.epam.gymapp.api.filter;

import com.epam.gymapp.repositories.RevokedTokenRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class RevocationFilter extends OncePerRequestFilter {

  private final RevokedTokenRepo repo;

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    String hdr = req.getHeader(HttpHeaders.AUTHORIZATION);

    if (hdr != null && hdr.startsWith("Bearer ")) {
      String token = hdr.substring(7);
      if (repo.existsById(token)) {
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Revoked Token");
        return;
      }
    }
    chain.doFilter(req, res);
  }
}
