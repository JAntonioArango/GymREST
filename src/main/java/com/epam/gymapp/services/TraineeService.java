package com.epam.gymapp.services;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

import com.epam.gymapp.api.advice.ApiException;
import com.epam.gymapp.api.dto.*;
import com.epam.gymapp.entities.Trainee;
import com.epam.gymapp.entities.Trainer;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.TraineeRepo;
import com.epam.gymapp.repositories.TrainerRepo;
import com.epam.gymapp.repositories.UserRepo;
import com.epam.gymapp.utils.CredentialGenerator;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TraineeService {

  private final TraineeRepo traineeRepo;  private final CredentialGenerator creds;
  private final TrainerRepo trainerRepo;  private final UserRepo userRepo;

  public TraineeRegistrationDto register(CreateTraineeDto dto) {

    if (trainerRepo.existsByUserFirstNameAndUserLastName(dto.firstName(), dto.lastName())) {
      throw ApiException.badRequest(
          "Cannot register as trainee; a trainer with the same name already exists");
    }

    TraineeDto saved = createProfile(dto);

    User u =
        userRepo
            .findByUsername(saved.username())
            .orElseThrow(() -> ApiException.notFound("User", saved.username()));

    return new TraineeRegistrationDto(u.getUsername(), u.getPassword());
  }

  public TraineeDto createProfile(CreateTraineeDto dto) {

    User u = new User();
    u.setFirstName(dto.firstName());
    u.setLastName(dto.lastName());
    u.setUsername(creds.buildUniqueUsername(dto.firstName(), dto.lastName()));
    u.setPassword(creds.randomPassword());
    u.setActive(true);

    Trainee t = new Trainee();
    t.setUser(u);
    t.setDateOfBirth(dto.dateOfBirth());
    t.setAddress(dto.address());

    traineeRepo.save(t);
    return toDto(t);
  }

  public TraineeDto findByUsername(String username) {

    return traineeRepo
        .findByUserUsername(username)
        .map(this::toDto)
        .orElseThrow(() -> ApiException.notFound("Trainee", id));
  }

  public TraineeProfileDto updateProfile(String username, UpdateTraineeDto dto) {

    Trainee t =
        traineeRepo
            .findByUserUsername(username)
            .orElseThrow(() -> ApiException.notFound("Trainee", username));

    t.getUser().setFirstName(dto.firstName());
    t.getUser().setLastName(dto.lastName());

    if (dto.dateOfBirth() != null) t.setDateOfBirth(dto.dateOfBirth());
    if (dto.address() != null) t.setAddress(dto.address());

    t.getUser().setActive(dto.isActive());

    return toProfileDto(t);
  }

  @Transactional
  public void deleteByUsername(String username) {

    Trainee trainee =
        traineeRepo
            .findByUserUsername(username)
            .orElseThrow(() -> ApiException.notFound("Trainee", username));

    // detach from trainers
    trainee.getTrainers().forEach(tr -> tr.getTrainees().remove(trainee));
    trainee.getTrainers().clear();

    traineeRepo.delete(trainee); // JPA cascades remove to trainings
  }

  public void changePassword(String username, String currentPwd, String newPwd) {

    // 1) authenticate
    boolean ok = traineeRepo.existsByUserUsernameAndUserPassword(username, currentPwd);
    if (!ok) throw ApiException.badCredentials();

    // 2) update
    Trainee trainee =
        traineeRepo
            .findByUserUsername(username)
            .orElseThrow(() -> ApiException.notFound("Trainee", username));

    trainee.getUser().setPassword(newPwd);
  }

  public TraineeDto setActive(String username, boolean active) {

    Trainee trainee =
        traineeRepo
            .findByUserUsername(username)
            .orElseThrow(() -> ApiException.notFound("Trainee", username));

    trainee.getUser().setActive(active);
    return toDto(trainee);
  }

  public TraineeDto updateTrainers(String traineeUsername, Set<Long> trainerIds) {

    Trainee trainee =
        traineeRepo
            .findByUserUsername(traineeUsername)
            .orElseThrow(() -> ApiException.notFound("Trainee", traineeUsername));

    List<Trainer> trainers = trainerRepo.findAllById(trainerIds);
    verifySize(trainerIds, trainers);

    for (Trainer old : new HashSet<>(trainee.getTrainers())) {
      old.getTrainees().remove(trainee);
    }
    trainee.getTrainers().clear();

    addNewLines(trainers, trainee);

    return toDto(trainee);
  }

  private static void verifySize(Set<Long> trainerIds, List<Trainer> trainers) {
    if (trainers.size() != trainerIds.size()) {
      throw ApiException.badRequest("One or more trainer IDs not found");
    }
  }

  private static void addNewLines(List<Trainer> trainers, Trainee trainee) {
    for (Trainer t : trainers) {
      trainee.getTrainers().add(t);
      t.getTrainees().add(trainee);
    }
  }

  public Page<TraineeDto> list(Pageable pageable) {
    return traineeRepo.findAll(pageable).map(this::toDto);
  }

  private TraineeDto toDto(Trainee t) {
    return new TraineeDto(
        t.getUser().getUsername(),
        t.getUser().getFirstName(),
        t.getUser().getLastName(),
        t.getDateOfBirth(),
        t.getAddress(),
        t.getUser().isActive());
  }

  public TraineeProfileDto findProfile(String username) {

    Trainee t =
        traineeRepo
            .findByUserUsername(username)
            .orElseThrow(() -> ApiException.notFound("Trainee", username));

    return toProfileDto(t);
  }

  private TraineeProfileDto toProfileDto(Trainee t) {

    List<TrainerShortDto> trainerDtos =
        t.getTrainers().stream()
            .map(
                tr ->
                    new TrainerShortDto(
                        tr.getUser().getUsername(),
                        tr.getUser().getFirstName(),
                        tr.getUser().getLastName(),
                        tr.getSpecialization().getName()))
            .toList();

    return new TraineeProfileDto(
        t.getUser().getUsername(),
        t.getUser().getFirstName(),
        t.getUser().getLastName(),
        t.getDateOfBirth(),
        t.getAddress(),
        t.getUser().isActive(),
        trainerDtos);
  }

  public List<TrainerShortDto> replaceTrainers(
      String traineeUsername, List<String> trainerUsernames) {

    // 1) Load trainee
    Trainee trainee =
        traineeRepo
            .findByUserUsername(traineeUsername)
            .orElseThrow(() -> ApiException.notFound("Trainee", traineeUsername));

    // 2) Fetch all trainers whose username is in the list
    List<Trainer> trainers = trainerRepo.findByUserUsernameIn(trainerUsernames);

    // 3) Verify all usernames exist
    if (trainers.size() != trainerUsernames.size()) {
      throw ApiException.notFound("Trainer IDs", String.join(",", trainerUsernames));
    }

    // 4) Replace the association
    trainee.setTrainers(new HashSet<>(trainers));

    // 5) Return mapped list
    return trainers.stream()
        .map(
            tr ->
                new TrainerShortDto(
                    tr.getUser().getUsername(),
                    tr.getUser().getFirstName(),
                    tr.getUser().getLastName(),
                    tr.getSpecialization().getName()))
        .toList();
  }
}
