package com.epam.gymapp.api.advice;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{

    private final HttpStatus status;

    private ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static ApiException notFound(String entity, Object idOrName) {
        return new ApiException(
                HttpStatus.NOT_FOUND,
                entity + " not found: " + idOrName);
    }

    public static ApiException badCredentials() {
        return new ApiException(
                HttpStatus.UNAUTHORIZED,
                "Invalid username or password");
    }

    public static ApiException badRequest(String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, message);
    }
}
