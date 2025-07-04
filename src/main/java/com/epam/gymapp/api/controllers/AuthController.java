package com.epam.gymapp.api.controllers;

import com.epam.gymapp.api.dto.ChangePasswordDto;
import com.epam.gymapp.api.dto.TokenDto;
import com.epam.gymapp.entities.RevokedToken;
import com.epam.gymapp.repositories.RevokedTokenRepo;
import com.epam.gymapp.services.AuthenticationService;
import com.epam.gymapp.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

  private final AuthenticationService authService;
  private final JwtService jwtService;
  private final RevokedTokenRepo repo;

  @GetMapping("/login")
  @Operation(summary = "Login (3)")
  public ResponseEntity<TokenDto> login(
      @RequestParam String username, @RequestParam String password) {

    authService.validate(username, password);

    String token = jwtService.createToken(username);

    return ResponseEntity.ok(new TokenDto(token));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, Authentication auth) {
    String hdr = request.getHeader(HttpHeaders.AUTHORIZATION);
    String token = hdr.substring(7);
    var jwt = jwtService.parse(token);

    repo.save(new RevokedToken(token, jwt.getExpiresAt()));

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{username}/password")
  @Operation(summary = "Change Password (4)")
  public ResponseEntity<Void> changePassword(
      @PathVariable String username, @Valid @RequestBody ChangePasswordDto body) {

    authService.changePassword(username, body.oldPassword(), body.newPassword());
    return ResponseEntity.ok().build();
  }
}
