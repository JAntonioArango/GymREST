package com.epam.gymapp.services;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.api.dto.*;
import com.epam.gymapp.entities.Trainer;
import com.epam.gymapp.entities.TrainingType;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.TraineeRepo;
import com.epam.gymapp.repositories.TrainerRepo;
import com.epam.gymapp.repositories.TrainingTypeRepo;
import com.epam.gymapp.repositories.UserRepository;
import com.epam.gymapp.utils.CredentialGenerator;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainerService {

  private final TrainerRepo trainerRepo;
  private final TrainingTypeRepo typeRepo;
  private final CredentialGenerator creds;
  private final TraineeRepo traineeRepo;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public TrainerRegistrationDto register(CreateTrainerDto dto) {

    String rawPassword = creds.randomPassword();
    String username = creds.buildUniqueUsername(dto.firstName(), dto.lastName());

    User u = new User();
    u.setFirstName(dto.firstName());
    u.setLastName(dto.lastName());
    u.setUsername(username);
    u.setPassword(passwordEncoder.encode(rawPassword));
    u.setActive(true);

    Trainer t = new Trainer();
    t.setUser(u);

    TrainingType type = typeRepo.findByName(dto.specialization())
                    .orElseThrow(() -> ApiException.notFound("Training type", dto.specialization().name()));

    t.setSpecialization(type);
    trainerRepo.save(t);

    return new TrainerRegistrationDto(username, rawPassword);
  }

  public TrainerDto createProfile(CreateTrainerDto dto) {

    TrainingType spec =
        typeRepo
            .findByName(dto.specialization())
            .orElseThrow(() -> ApiException.notFound("Training type", dto.specialization().name()));

    User u =
        new User(
            null,
            dto.firstName(),
            dto.lastName(),
            creds.buildUniqueUsername(dto.firstName(), dto.lastName()),
            creds.randomPassword(),
            true,
            null,
            null);

    Trainer t = new Trainer();
    t.setUser(u);
    t.setSpecialization(spec);

    trainerRepo.save(t);
    return toDto(t);
  }

  public TrainerDto findByUsername(String username) {
    return trainerRepo
        .findByUserUsername(username)
        .map(this::toDto)
        .orElseThrow(() -> ApiException.notFound("Trainer", username));
  }

  public TrainerProfileDto updateProfile(String username, UpdateTrainerDto dto) {

    Trainer trainer =
        trainerRepo
            .findByUserUsername(username)
            .orElseThrow(() -> ApiException.notFound("Trainer", username));

    trainerRepo.findByUserUsername(dto.username()).ifPresent(
        t -> {
          if (!t.getId().equals(trainer.getId())) {
              try {
                  throw ApiException.duplicate("Username", dto.username());
              } catch (Exception e) {
                  throw new RuntimeException(e);
              }
          }
        });

    trainer.getUser().setUsername(dto.username().replaceAll("\\s", "." ));
    trainer.getUser().setFirstName(dto.firstName());
    trainer.getUser().setLastName(dto.lastName());
    trainer.getUser().setActive(dto.isActive());

    return toProfileDto(trainer);
  }

  public void changePassword(String username, String currentPwd, String newPwd) {

    boolean ok = trainerRepo.existsByUserUsernameAndUserPassword(username, currentPwd);
    if (!ok) throw ApiException.badCredentials();

    Trainer trainer =
        trainerRepo
            .findByUserUsername(username)
            .orElseThrow(() -> ApiException.notFound("Trainer", username));

    trainer.getUser().setPassword(newPwd);
  }

  public TrainerDto setActive(String username, boolean active) {

    Trainer trainer =
        trainerRepo
            .findByUserUsername(username)
            .orElseThrow(() -> ApiException.notFound("Trainer", username));

    trainer.getUser().setActive(active);
    return toDto(trainer);
  }

  public Page<TrainerDto> list(Pageable pageable) {
    return trainerRepo.findAll(pageable).map(this::toDto);
  }

  private TrainerDto toDto(Trainer t) {
    return new TrainerDto(
        t.getId(),
        t.getUser().getUsername(),
        t.getUser().isActive(),
        t.getUser().getFirstName(),
        t.getUser().getLastName(),
        t.getSpecialization().getName());
  }

  public TrainerProfileDto findProfile(String username) {

    Trainer trainer =
        trainerRepo
            .findByUserUsername(username)
            .orElseThrow(() -> ApiException.notFound("Trainer", username));

    return toProfileDto(trainer);
  }

  private TrainerProfileDto toProfileDto(Trainer t) {

    List<TraineeShortDto> traineeDtos =
        t.getTrainees().stream()
            .map(
                tr ->
                    new TraineeShortDto(
                        tr.getUser().getUsername(),
                        tr.getUser().getFirstName(),
                        tr.getUser().getLastName()))
            .toList();

    return new TrainerProfileDto(
        t.getUser().getUsername(),
        t.getUser().getFirstName(),
        t.getUser().getLastName(),
        t.getSpecialization().getName(),
        t.getUser().isActive(),
        traineeDtos);
  }

  public List<TrainerShortDto> unassignedActiveTrainers(String traineeUsername) {
    traineeRepo
        .findByUserUsername(traineeUsername)
        .orElseThrow(() -> ApiException.notFound("Trainee", traineeUsername));

    Set<Long> assignedIds =
        trainerRepo.findByTraineesUserUsername(traineeUsername).stream()
            .map(Trainer::getId)
            .collect(Collectors.toSet());

    return trainerRepo.findAll().stream()
        .filter(t -> !assignedIds.contains(t.getId()) && t.getUser().isActive())
        .map(
            t ->
                new TrainerShortDto(
                    t.getUser().getUsername(),
                    t.getUser().getFirstName(),
                    t.getUser().getLastName(),
                    t.getSpecialization().getName()))
        .toList();
  }
}
