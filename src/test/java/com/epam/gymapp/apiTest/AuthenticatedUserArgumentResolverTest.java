package com.epam.gymapp.apiTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.api.auth.AuthenticatedUser;
import com.epam.gymapp.api.auth.AuthenticatedUserArgumentResolver;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

class AuthenticatedUserArgumentResolverTest {

  private AuthenticatedUserArgumentResolver resolver;

  @Mock private AuthenticationService authService;
  @Mock private NativeWebRequest webRequest;
  @Mock private MethodParameter parameter;
  @Mock private ModelAndViewContainer mavContainer;
  @Mock private WebDataBinderFactory binderFactory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    resolver = new AuthenticatedUserArgumentResolver(authService);
  }

  @Test
  void supportsParameter_shouldReturnTrueForValidParameter() throws NoSuchMethodException {
    MethodParameter param =
        new MethodParameter(TestController.class.getMethod("validMethod", User.class), 0);
    assertTrue(resolver.supportsParameter(param));
  }

  @Test
  void supportsParameter_shouldReturnFalseForNonAnnotatedParameter() throws NoSuchMethodException {
    MethodParameter param =
        new MethodParameter(TestController.class.getMethod("nonAnnotatedMethod", User.class), 0);
    assertFalse(resolver.supportsParameter(param));
  }

  @Test
  void supportsParameter_shouldReturnFalseForNonUserParameter() throws NoSuchMethodException {
    MethodParameter param =
        new MethodParameter(TestController.class.getMethod("wrongTypeMethod", String.class), 0);
    assertFalse(resolver.supportsParameter(param));
  }

  @Test
  void resolveArgument_shouldReturnUserWhenCredentialsPresent() throws Exception {
    // Arrange
    when(webRequest.getParameter("username")).thenReturn("john");
    when(webRequest.getParameter("password")).thenReturn("secret");
    User expected = new User();
    when(authService.validate("john", "secret")).thenReturn(expected);

    MethodParameter fakeParam = mock(MethodParameter.class);
    // Act
    Object result = resolver.resolveArgument(fakeParam, mavContainer, webRequest, binderFactory);

    // Assert
    assertSame(expected, result);
    verify(authService).validate("john", "secret");
  }

  @Test
  void resolveArgument_shouldThrowWhenUsernameBlank() {
    when(webRequest.getParameter("username")).thenReturn("   ");
    when(webRequest.getParameter("password")).thenReturn("pass");

    assertThrows(
        ApiException.class,
        () -> resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory));
  }

  @Test
  void resolveArgument_shouldThrowWhenPasswordBlank() {
    when(webRequest.getParameter("username")).thenReturn("john");
    when(webRequest.getParameter("password")).thenReturn("");

    assertThrows(
        ApiException.class,
        () -> resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory));
  }

  // Dummy controller for reflection
  private static class TestController {
    public void validMethod(@AuthenticatedUser User user) {}

    public void nonAnnotatedMethod(User user) {}

    public void wrongTypeMethod(@AuthenticatedUser String val) {}
  }
}
