package com.epam.gymapp.api.advice;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "Standardized API error codes and titles")
public enum ErrorCode {
  @Schema(description = "Resource not found")
  RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource Not Found"),

  @Schema(description = "Invalid request or validation failure")
  VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation Failed"),

  @Schema(description = "Unauthorized access")
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),

  @Schema(description = "Internal server error")
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

  private final HttpStatus status;
  private final String title;

  ErrorCode(HttpStatus status, String title) {
    this.status = status;
    this.title = title;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getTitle() {
    return title;
  }
}
