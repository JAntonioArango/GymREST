package com.epam.gymapp.trainee;

import com.epam.gymapp.dto.CreateTraineeDto;
import com.epam.gymapp.dto.TraineeDto;
import com.epam.gymapp.entities.*;
import com.epam.gymapp.repositories.TraineeRepo;
import com.epam.gymapp.repositories.TrainerRepo;
import com.epam.gymapp.services.TraineeService;
import com.epam.gymapp.utils.CredentialGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)          // ← avoid UnnecessaryStubbing
class ServiceTest {

    @Mock TraineeRepo        traineeRepo;
    @Mock TrainerRepo        trainerRepo;
    @Mock CredentialGenerator creds;

    @InjectMocks TraineeService service;

    private final LocalDate DOB = LocalDate.of(1990, 3, 4);

    @BeforeEach
    void stubs() {
        when(creds.buildUniqueUsername("John", "Smith")).thenReturn("john.smith");
        when(creds.randomPassword()).thenReturn("Pwd1234567");
    }

    @Test
    void createProfile_happyPath() {
        CreateTraineeDto dto =
                new CreateTraineeDto("John", "Smith", DOB, "Address");

        TraineeDto out = service.createProfile(dto);

        assertThat(out.username()).isEqualTo("john.smith");
        verify(traineeRepo).save(any(Trainee.class));
    }

    @Test
    void authenticate_returnsTrueWhenMatch() {
        when(traineeRepo.existsByUserUsernameAndUserPassword("john","pwd"))
                .thenReturn(true);

        assertThat(service.authenticate("john","pwd")).isTrue();
    }

    @Test
    void findByUsername_returnsDtoWhenCredentialsOk() throws AccessDeniedException {
        Trainee entity = stubTrainee();

        when(traineeRepo.existsByUserUsernameAndUserPassword("john.smith","Pwd123"))
                .thenReturn(true);
        when(traineeRepo.findByUserUsername("john.smith"))
                .thenReturn(Optional.of(entity));

        TraineeDto dto = service.findByUsername("john.smith","Pwd123");

        assertThat(dto.firstName()).isEqualTo("John");
    }

    @Test
    void findByUsername_throwsWhenBadPassword() {
        when(traineeRepo.existsByUserUsernameAndUserPassword("john.smith","bad"))
                .thenReturn(false);

        assertThatThrownBy(
                () -> service.findByUsername("john.smith","bad"))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void updateTrainers_replacesList() throws java.nio.file.AccessDeniedException {
        Trainee trainee = stubTrainee();
        Trainer t1 = trainer(1L), t2 = trainer(2L);

        when(traineeRepo.existsByUserUsernameAndUserPassword("john.smith","Pwd123"))
                .thenReturn(true);
        when(traineeRepo.findByUserUsername("john.smith"))
                .thenReturn(Optional.of(trainee));
        when(trainerRepo.findAllById(Set.of(1L, 2L)))
                .thenReturn(List.of(t1, t2));

        TraineeDto dto = service.updateTrainers(
                "john.smith","Pwd123",
                "john.smith",
                Set.of(1L, 2L));

        assertThat(dto.id()).isEqualTo(trainee.getId());
        assertThat(trainee.getTrainers()).containsExactlyInAnyOrder(t1, t2);
    }

    private Trainee stubTrainee() {
        User u = new User(10L, "John", "Smith",
                "john.smith", "Pwd123", true, null, null);
        return new Trainee(20L, DOB, "Address", u,
                new HashSet<>(), new HashSet<>());
    }

    private Trainer trainer(Long id) {
        TrainingType type = new TrainingType();
        type.setId(99L);
        type.setName("CARDIO");

        User u = new User();
        u.setId(id + 100);
        u.setFirstName("T");
        u.setLastName("R" + id);
        u.setUsername("t" + id);
        u.setPassword("pwd");
        u.setActive(true);

        Trainer t = new Trainer();
        t.setId(id);
        t.setSpecialization(type);
        t.setUser(u);
        t.setTrainees(new HashSet<>());
        t.setTrainings(new HashSet<>());
        return t;
    }
}
