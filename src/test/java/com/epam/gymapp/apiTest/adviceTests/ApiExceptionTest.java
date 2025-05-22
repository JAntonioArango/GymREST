package com.epam.gymapp.apiTest.adviceTests;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gymapp.api.advice.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiExceptionTest {
  @Test
  void testNotFoundFactory() {
    ApiException ex = ApiException.notFound("User", 123);
    assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    assertTrue(ex.getMessage().contains("User not found: 123"));
  }

  @Test
  void testBadCredentialsFactory() {
    ApiException ex = ApiException.badCredentials();
    assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
    assertEquals("Invalid username or password", ex.getMessage());
  }

  @Test
  void testBadRequestFactory() {
    ApiException ex = ApiException.badRequest("Invalid input");
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    assertEquals("Invalid input", ex.getMessage());
  }
}
