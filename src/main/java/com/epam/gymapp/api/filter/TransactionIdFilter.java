package com.epam.gymapp.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TransactionIdFilter extends OncePerRequestFilter {

  private static final String HEADER = "X-Transaction-Id";
  private static final String MDC_KEY = "txId";

  @Override
  public void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String txId =
        Optional.ofNullable(request.getHeader(HEADER))
            .filter(h -> !h.isBlank())
            .orElse(UUID.randomUUID().toString());

    MDC.put(MDC_KEY, txId);

    log.info(
        "==== Start transaction {} for {} {} ====",
        txId,
        request.getMethod(),
        request.getRequestURI());

    long start = System.currentTimeMillis();
    try {
      chain.doFilter(request, response);
    } finally {
      long elapsed = System.currentTimeMillis() - start;

      log.info("==== End   transaction {} → {} ({} ms) ====", txId, response.getStatus(), elapsed);

      MDC.remove(MDC_KEY);
    }
  }
}
