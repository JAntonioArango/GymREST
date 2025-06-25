package com.epam.gymapp.api.auth;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

  private final UserRepository userRepo;

  @Override
  public boolean supportsParameter(MethodParameter p) {
    return p.getParameterType().equals(User.class)
        && p.hasParameterAnnotation(AuthenticatedUser.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter p, ModelAndViewContainer mav, NativeWebRequest web, WebDataBinderFactory b) {

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return userRepo
        .findByUsername(username)
        .orElseThrow(() -> ApiException.notFound("User", username));
  }
}
