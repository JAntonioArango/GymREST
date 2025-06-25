package com.epam.gymapp.config;

import com.epam.gymapp.repositories.UserRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.*;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserRepository userRepo;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return username ->
        userRepo
            .findByUsername(username)
            .map(
                u ->
                    User.builder()
                        .username(u.getUsername())
                        .password(u.getPassword())
                        .roles("USER")
                        .build())
            .orElseThrow(
                () -> new UsernameNotFoundException("User %s not found".formatted(username)));
  }

  @Bean
  @Transactional
  public SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder)
      throws Exception {

    http.csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsSource()))
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/api/v1/trainer/register",
                        "/api/v1/trainee/register",
                        "/api/v1/auth/login")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(
                    jwt ->
                        jwt.decoder(jwtDecoder)
                            .jwtAuthenticationConverter(new UsernameSubConverter())))
        .logout(
            logout ->
                logout
                    .logoutUrl("/api/v1/auth/logout")
                    .logoutSuccessHandler(
                        (req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value()))
                    .addLogoutHandler((req, res, auth) -> SecurityContextHolder.clearContext()))
        .httpBasic(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      UserDetailsService details, PasswordEncoder encoder) {

    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(details);
    provider.setPasswordEncoder(encoder);
    return new ProviderManager(provider);
  }

  private CorsConfigurationSource corsSource() {
    CorsConfiguration cfg = new CorsConfiguration();
    String origins = Optional.ofNullable(System.getenv("ALLOWED_ORIGINS")).orElse("*");
    cfg.setAllowedOrigins(List.of(origins.split(",")));
    cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD"));
    cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    cfg.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
    src.registerCorsConfiguration("/**", cfg);
    return src;
  }

  private static class UsernameSubConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
      Collection<GrantedAuthority> auth = List.of(new SimpleGrantedAuthority("ROLE_USER"));
      return new JwtAuthenticationToken(jwt, auth, jwt.getSubject());
    }
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    String csv =
        Optional.ofNullable(System.getenv("ALLOWED_ORIGINS"))
            .orElse(
                "http://localhost:4200"); // most common default for front-end development in the
    // Spring-Boot world

    CorsConfiguration cfg = new CorsConfiguration();
    cfg.setAllowedOrigins(List.of(csv.split(",")));

    cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD"));

    cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));

    cfg.setAllowCredentials(true);

    cfg.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cfg);
    return source;
  }
}
