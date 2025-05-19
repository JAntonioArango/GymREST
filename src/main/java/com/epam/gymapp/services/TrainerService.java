package com.epam.gymapp.services;

import com.epam.gymapp.dto.CreateTrainerDto;
import com.epam.gymapp.dto.TrainerDto;
import com.epam.gymapp.entities.TrainingType;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.exceptions.BadCredentialsException;
import com.epam.gymapp.exceptions.TrainerNotFoundException;
import com.epam.gymapp.entities.Trainer;
import com.epam.gymapp.repositories.TrainerRepo;
import com.epam.gymapp.repositories.TrainingTypeRepo;
import com.epam.gymapp.utils.CredentialGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepo trainerRepo;
    private final TrainingTypeRepo typeRepo;
    private final CredentialGenerator creds;

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    public TrainerDto createProfile(CreateTrainerDto dto) {

        TrainingType spec = typeRepo.findByName(dto.specialization())
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: "
                        + dto.specialization()));

        User u = new User(
                null,
                dto.firstName(), dto.lastName(),
                creds.buildUniqueUsername(dto.firstName(), dto.lastName()),
                creds.randomPassword(),
                true, null, null);

        Trainer t = new Trainer();
        t.setUser(u);
        t.setSpecialization(spec);

        trainerRepo.save(t);
        return toDto(t);
    }

    public TrainerDto findByUsername(String username, String password) throws AccessDeniedException {
        if (!authenticate(username, password)) {
            throw new AccessDeniedException("Bad credentials");
        }
        return trainerRepo.findByUserUsername(username)
                .map(this::toDto)
                .orElseThrow(() -> new TrainerNotFoundException(username));
    }

    public TrainerDto updateProfile(String username, String password, CreateTrainerDto dto) throws AccessDeniedException {
        if (!authenticate(username, password)) {
            throw new AccessDeniedException("Bad credentials");
        }

        Trainer trainer = trainerRepo.findByUserUsername(username)
                .orElseThrow(() -> new TrainerNotFoundException(username));

        trainer.getUser().setFirstName(dto.firstName());
        trainer.getUser().setLastName(dto.lastName());

        checker(dto, trainer);

        return toDto(trainer);
    }

    private void checker(CreateTrainerDto dto, Trainer trainer) {
        if (dto.specialization() != null &&
                !dto.specialization().equals(trainer.getSpecialization().getName())) {

            TrainingType spec = typeRepo.findByName(dto.specialization())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Unknown specialization"));
            trainer.setSpecialization(spec);
        }
    }

    public boolean authenticate(String username, String rawPassword) {
        return trainerRepo.existsByUserUsernameAndUserPassword(username, rawPassword);
    }

    public void changePassword(String username, String currentPwd, String newPwd) throws AccessDeniedException {
        if (!authenticate(username, currentPwd)) {
            throw new AccessDeniedException("Bad credentials");
        }

        boolean ok = trainerRepo.existsByUserUsernameAndUserPassword(username, currentPwd);
        if (!ok) throw new BadCredentialsException("Current password is incorrect");

        Trainer trainer = trainerRepo.findByUserUsername(username)
                .orElseThrow(() -> new TrainerNotFoundException(username));

        trainer.getUser().setPassword(newPwd);
    }

    public void deactivateTrainee(String username, String password) throws AccessDeniedException {
        setActive(username, password, false);
    }

    public void activateTrainee(String username, String password) throws AccessDeniedException {
        setActive(username, password, true);
    }

    public void setActive(String username, String password, boolean active) throws AccessDeniedException {
        if (!authenticate(username, password)) {
            throw new AccessDeniedException("Bad credentials");
        }
        Trainer trainer = trainerRepo.findByUserUsername(username)
                .orElseThrow(() -> new TrainerNotFoundException(username));

        trainer.getUser().setActive(active);
    }

    public List<TrainerDto> unassignedTrainers(String username, String password, String traineeUsername) throws AccessDeniedException {
        if (!authenticate(username, password)) {
            throw new AccessDeniedException("Bad credentials");
        }
        List<Trainer> assigned = trainerRepo
                .findByTraineesUserUsername(traineeUsername);

        Set<Long> assignedIds = assigned.stream()
                .map(Trainer::getId)
                .collect(Collectors.toSet());

        return trainerRepo.findAll().stream()
                .filter(t -> !assignedIds.contains(t.getId()))
                .map(this::toDto)
                .toList();
    }



    private TrainerDto toDto(Trainer t) {
        return new TrainerDto(
                t.getId(),
                t.getUser().getUsername(),
                t.getUser().isActive(),
                t.getUser().getFirstName(),
                t.getUser().getLastName(),
                t.getSpecialization().getName()
        );
    }
}
