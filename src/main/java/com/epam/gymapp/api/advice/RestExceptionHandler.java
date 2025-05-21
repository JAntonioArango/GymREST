package com.epam.gymapp.api.advice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ProblemDetail> notFound(EntityNotFoundException ex,
                                           HttpServletRequest req) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Resource not found");
        pd.setDetail(ex.getMessage());
        pd.setProperty("path", req.getRequestURI());

        return ResponseEntity.status(pd.getStatus()).body(pd);
    }

    @ExceptionHandler(ApiException.class)
    ResponseEntity<ProblemDetail> apiError(ApiException ex,
                                           HttpServletRequest req) {

        ProblemDetail pd = ProblemDetail.forStatus(ex.getStatus());
        pd.setTitle(ex.getStatus().getReasonPhrase());
        pd.setDetail(ex.getMessage());
        pd.setProperty("path", req.getRequestURI());
        return ResponseEntity.status(ex.getStatus()).body(pd);
    }
}
