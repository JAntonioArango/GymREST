package com.epam.gymapp.apiTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.api.auth.AuthenticatedUser;
import com.epam.gymapp.api.auth.AuthenticatedUserArgumentResolver;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.UserRepository;
import com.epam.gymapp.services.AuthenticationService;
import java.lang.reflect.Method;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@ExtendWith(MockitoExtension.class)
class AuthenticatedUserArgumentResolverTest {

  private AuthenticatedUserArgumentResolver resolver;

  @Mock private UserRepository userRepo;
  @Mock private AuthenticationService authService;
  @Mock private NativeWebRequest webRequest;
  @Mock private MethodParameter parameter;
  @Mock private ModelAndViewContainer mavContainer;
  @Mock private WebDataBinderFactory binderFactory;

  @BeforeEach
  void setUp() {
    resolver = new AuthenticatedUserArgumentResolver(userRepo);
    SecurityContextHolder.clearContext();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void supportsParameter_returnsTrueForAnnotatedUser() throws NoSuchMethodException {
    Method m = TestController.class.getMethod("validMethod", User.class);
    MethodParameter p = new MethodParameter(m, 0);
    assertTrue(resolver.supportsParameter(p));
  }

  @Test
  void supportsParameter_returnsFalseWhenNotAnnotated() throws NoSuchMethodException {
    Method m = TestController.class.getMethod("nonAnnotatedMethod", User.class);
    MethodParameter p = new MethodParameter(m, 0);
    assertFalse(resolver.supportsParameter(p));
  }

  @Test
  void supportsParameter_returnsFalseWhenWrongType() throws NoSuchMethodException {
    Method m = TestController.class.getMethod("wrongTypeMethod", String.class);
    MethodParameter p = new MethodParameter(m, 0);
    assertFalse(resolver.supportsParameter(p));
  }

  @Test
  void resolveArgument_returnsUserFromRepository() throws Exception {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("john", "N/A"));

    User expected = new User();
    when(userRepo.findByUsername("john")).thenReturn(java.util.Optional.of(expected));

    Object result =
        resolver.resolveArgument(
            mock(MethodParameter.class), mavContainer, webRequest, binderFactory);

    assertSame(expected, result);
    verify(userRepo).findByUsername("john");
  }

  @Test
  void resolveArgument_throwsWhenUserMissing() {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("ghost", "N/A"));

    when(userRepo.findByUsername("ghost")).thenReturn(java.util.Optional.empty());

    assertThrows(
        ApiException.class,
        () -> resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory));
  }

  private static class TestController {
    public void validMethod(@AuthenticatedUser User user) {}

    public void nonAnnotatedMethod(User user) {}

    public void wrongTypeMethod(@AuthenticatedUser String val) {}
  }
}
