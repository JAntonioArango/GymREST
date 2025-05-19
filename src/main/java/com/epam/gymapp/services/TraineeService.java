package com.epam.gymapp.services;

import com.epam.gymapp.dto.CreateTraineeDto;
import com.epam.gymapp.dto.TraineeDto;
import com.epam.gymapp.entities.Trainer;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.entities.Trainee;
import com.epam.gymapp.exceptions.BadCredentialsException;
import com.epam.gymapp.exceptions.TraineeNotFoundException;
import com.epam.gymapp.repositories.TraineeRepo;
import com.epam.gymapp.repositories.TrainerRepo;
import com.epam.gymapp.utils.CredentialGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeRepo traineeRepo;
    private final CredentialGenerator creds;
    private final TrainerRepo trainerRepo;

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

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

    public TraineeDto findByUsername(String username, String password) throws AccessDeniedException {
        if (!authenticate(username, password)) {
            throw new AccessDeniedException("Bad credentials");
        }
        return traineeRepo.findByUserUsername(username)
                .map(this::toDto)                         // ← mapper you already have
                .orElseThrow(() -> new TraineeNotFoundException(username));
    }

    public TraineeDto updateProfile(String username, String password, CreateTraineeDto dto) throws AccessDeniedException {
        if (!authenticate(username, password)) {
            throw new AccessDeniedException("Bad credentials");
        }

        Trainee trainee = traineeRepo.findByUserUsername(username)
                .orElseThrow(() -> new TraineeNotFoundException(username));

        trainee.getUser().setFirstName(dto.firstName());
        trainee.getUser().setLastName(dto.lastName());
        trainee.setDateOfBirth(dto.dateOfBirth());
        trainee.setAddress(dto.address());

        return toDto(trainee);
    }

    public void deleteByUsername(String username, String password) throws AccessDeniedException {
        if (!authenticate(username, password)) {
            throw new AccessDeniedException("Bad credentials");
        }
        if (!traineeRepo.existsByUserUsername(username)) {
            throw new TraineeNotFoundException(username);
        }
        traineeRepo.deleteByUserUsername(username);
    }

    public boolean authenticate(String username, String rawPassword) {
        return traineeRepo.existsByUserUsernameAndUserPassword(username, rawPassword);
    }

    public void changePassword(String username, String currentPwd, String newPwd) throws AccessDeniedException {
        if (!authenticate(username, currentPwd)) {
            throw new AccessDeniedException("Bad credentials");
        }

        // 1) authenticate
        boolean ok = traineeRepo.existsByUserUsernameAndUserPassword(username, currentPwd);
        if (!ok) throw new BadCredentialsException("Current password is incorrect");

        // 2) update
        Trainee trainee = traineeRepo.findByUserUsername(username)
                .orElseThrow(() -> new TraineeNotFoundException(username));

        trainee.getUser().setPassword(newPwd);
    }

    public void deactivateTrainee(String username, String password) throws AccessDeniedException { setActive(username, password, false); }
    public void activateTrainee(String username, String password) throws AccessDeniedException { setActive(username, password, true); }

    private void setActive(String username, String password, boolean active) throws AccessDeniedException {
        if (!authenticate(username, password)) {
            throw new AccessDeniedException("Bad credentials");
        }
        Trainee trainee = traineeRepo.findByUserUsername(username)
                .orElseThrow(() -> new TraineeNotFoundException(username));

        trainee.getUser().setActive(active);
    }

    public TraineeDto updateTrainers(String username, String password, String traineeUsername, Set<Long> trainerIds) throws AccessDeniedException {
        if (!authenticate(username, password)) {
            throw new AccessDeniedException("Bad credentials");
        }
        Trainee trainee = traineeRepo.findByUserUsername(traineeUsername)
                .orElseThrow(() -> new TraineeNotFoundException(traineeUsername));

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
            throw new IllegalArgumentException("One or more trainer IDs not found");
        }
    }

    private static void addNewLines(List<Trainer> trainers, Trainee trainee) {
        for (Trainer t : trainers) {
            trainee.getTrainers().add(t);
            t.getTrainees().add(trainee);
        }
    }

    private TraineeDto toDto(Trainee t) {
        return new TraineeDto(
                t.getId(),
                t.getUser().getUsername(),
                t.getUser().isActive(),
                t.getUser().getFirstName(),
                t.getUser().getLastName(),
                t.getDateOfBirth(),
                t.getAddress()
        );
    }

}

