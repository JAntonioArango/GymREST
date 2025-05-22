package com.epam.gymapp.apiTest;

import com.epam.gymapp.api.auth.AuthenticatedUser;
import com.epam.gymapp.entities.User;

class TestController {
  public void validMethod(@AuthenticatedUser User user) {}

  public void nonAnnotatedMethod(User user) {}

  public void wrongTypeMethod(@AuthenticatedUser String string) {}
}
