package com.epam.gymapp.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TransactionIdFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-Transaction-Id";
    private static final String MDC_KEY = "txId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        // 1. Generate or extract incoming Tx-ID
        String txId = Optional.ofNullable(request.getHeader(HEADER))
                .filter(h -> !h.isBlank())
                .orElse(UUID.randomUUID().toString());

        // 2. Put it into MDC for all downstream logging
        MDC.put(MDC_KEY, txId);

        // 3. Log “start”
        log.info("==== Start transaction {} for {} {} ====",
                txId, request.getMethod(), request.getRequestURI());

        long start = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            // 4. Log “end”
            log.info("==== End   transaction {} → {} ({} ms) ====",
                    txId, response.getStatus(), elapsed);

            // 5. Clean up
            MDC.remove(MDC_KEY);
        }
    }
}
