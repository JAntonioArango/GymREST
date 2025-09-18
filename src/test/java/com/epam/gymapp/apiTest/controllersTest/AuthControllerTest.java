package com.epam.gymapp.apiTest.controllersTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.epam.gymapp.api.controllers.AuthController;
import com.epam.gymapp.services.AuthenticationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock private AuthenticationService authService;
  @InjectMocks private AuthController controller;

  //  @Test
  //  void login_shouldReturnOkAndCallService() {
  //    ResponseEntity<TokenDto> resp = controller.login("john", "pwd");
  //    assertEquals(HttpStatus.OK, resp.getStatusCode());
  //    verify(authService).validate("john", "pwd");
  //  }

  //  @Test
  //  void changePassword_shouldReturnOkAndCallService() {
  //    ChangePasswordDto body = new ChangePasswordDto("old", "new");
  //    ResponseEntity<Void> resp = controller.changePassword("john", body);
  //    assertEquals(HttpStatus.OK, resp.getStatusCode());
  //    verify(authService).changePassword("john", "old", "new");
  //  }
}
