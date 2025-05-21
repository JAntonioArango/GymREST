package com.epam.gymapp.api.auth;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

  private final AuthenticationService authService;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(AuthenticatedUser.class)
        && User.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {

    String username = webRequest.getParameter("username");
    String password = webRequest.getParameter("password");

    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      throw ApiException.badCredentials();
    }

    return authService.validate(username, password);
  }
}
