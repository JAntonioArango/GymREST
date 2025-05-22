package com.epam.gymapp.serviceTest;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.UserRepository;
import com.epam.gymapp.services.AuthenticationService;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

class AuthenticationServiceTest {

  private AuthenticationService service;

  private UserRepository repo;

  @BeforeEach
  void setUp() {
    repo =
        new UserRepository() {
          @Override
          public boolean existsByUsername(String candidate) {
            return false;
          }

          @Override
          public Optional<User> findByUsername(String username) {
            return Optional.empty();
          }

          @Override
          public Optional<User> findByUsernameAndPassword(String username, String password) {
            return Optional.empty();
          }

          @Override
          public void flush() {}

          @Override
          public <S extends User> S saveAndFlush(S entity) {
            return null;
          }

          @Override
          public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
            return List.of();
          }

          @Override
          public void deleteAllInBatch(Iterable<User> entities) {}

          @Override
          public void deleteAllByIdInBatch(Iterable<Long> longs) {}

          @Override
          public void deleteAllInBatch() {}

          @Override
          public User getOne(Long aLong) {
            return null;
          }

          @Override
          public User getById(Long aLong) {
            return null;
          }

          @Override
          public User getReferenceById(Long aLong) {
            return null;
          }

          @Override
          public <S extends User> List<S> findAll(Example<S> example) {
            return List.of();
          }

          @Override
          public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
            return List.of();
          }

          @Override
          public <S extends User> List<S> saveAll(Iterable<S> entities) {
            return List.of();
          }

          @Override
          public List<User> findAll() {
            return List.of();
          }

          @Override
          public List<User> findAllById(Iterable<Long> longs) {
            return List.of();
          }

          @Override
          public <S extends User> S save(S entity) {
            return null;
          }

          @Override
          public Optional<User> findById(Long aLong) {
            return Optional.empty();
          }

          @Override
          public boolean existsById(Long aLong) {
            return false;
          }

          @Override
          public long count() {
            return 0;
          }

          @Override
          public void deleteById(Long aLong) {}

          @Override
          public void delete(User entity) {}

          @Override
          public void deleteAllById(Iterable<? extends Long> longs) {}

          @Override
          public void deleteAll(Iterable<? extends User> entities) {}

          @Override
          public void deleteAll() {}

          @Override
          public List<User> findAll(Sort sort) {
            return List.of();
          }

          @Override
          public Page<User> findAll(Pageable pageable) {
            return null;
          }

          @Override
          public <S extends User> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
          }

          @Override
          public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
          }

          @Override
          public <S extends User> long count(Example<S> example) {
            return 0;
          }

          @Override
          public <S extends User> boolean exists(Example<S> example) {
            return false;
          }

          @Override
          public <S extends User, R> R findBy(
              Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
          }
        };
    service = new AuthenticationService((UserRepository) repo);
  }

  @Test
  void validate_shouldThrowWhenNoMatch() {
    // Act & Assert
    assertThrows(ApiException.class, () -> service.validate("x", "y"));
  }

  @Test
  void validate_shouldThrowWhenUsernameNull() {
    // Act & Assert
    assertThrows(ApiException.class, () -> service.validate(null, "password"));
  }

  @Test
  void validate_shouldThrowWhenPasswordNull() {
    // Act & Assert
    assertThrows(ApiException.class, () -> service.validate("username", null));
  }

  @Test
  void changePassword_shouldThrowWhenUserNotFound() {
    // Act & Assert
    assertThrows(ApiException.class, () -> service.changePassword("nonexistent", "old", "newpwd"));
  }
}
