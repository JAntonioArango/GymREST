package com.epam.gymapp.trainer;

import com.epam.gymapp.dto.*;
import com.epam.gymapp.entities.*;
import com.epam.gymapp.repositories.*;
import com.epam.gymapp.services.TrainerService;
import com.epam.gymapp.utils.CredentialGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


import java.nio.file.AccessDeniedException;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)        // CHANGED
class ServiceTest {

    @Mock  TrainerRepo        trainerRepo;
    @Mock  TrainingTypeRepo   typeRepo;
    @Mock  CredentialGenerator creds;

    @InjectMocks TrainerService service;

    private TrainingType cardio;

    @BeforeEach
    void stubs() {
        cardio = new TrainingType();
        cardio.setId(1L);
        cardio.setName("CARDIO");

        lenient().when(typeRepo.findByName("CARDIO"))     // CHANGED
                .thenReturn(Optional.of(cardio));
        lenient().when(creds.buildUniqueUsername("Anna","Doe"))
                .thenReturn("anna.doe");
        lenient().when(creds.randomPassword())
                .thenReturn("Pwd123");
    }

    @Test
    void createProfile_persistsTrainer() {
        CreateTrainerDto dto = new CreateTrainerDto("Anna","Doe","CARDIO");

        TrainerDto out = service.createProfile(dto);

        assertThat(out.username()).isEqualTo("anna.doe");
        verify(trainerRepo).save(any(Trainer.class));
    }

    @Test
    void findByUsername_returnsDtoWhenAuthOk() throws AccessDeniedException {
        Trainer entity = stubTrainer(7L);

        when(trainerRepo.existsByUserUsernameAndUserPassword("anna.doe","Pwd123"))
                .thenReturn(true);
        when(trainerRepo.findByUserUsername("anna.doe"))
                .thenReturn(Optional.of(entity));

        TrainerDto dto = service.findByUsername("anna.doe","Pwd123");

        assertThat(dto.firstName()).isEqualTo("A");
    }

    @Test
    void findByUsername_deniesBadPwd() {
        when(trainerRepo.existsByUserUsernameAndUserPassword("anna.doe","bad"))
                .thenReturn(false);                        // CHANGED
        assertThatThrownBy(
                () -> service.findByUsername("anna.doe","bad"))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void updateProfile_updatesNames() throws AccessDeniedException {
        Trainer entity = stubTrainer(11L);

        when(trainerRepo.existsByUserUsernameAndUserPassword("anna.doe","Pwd123"))
                .thenReturn(true);                         // CHANGED
        when(trainerRepo.findByUserUsername("anna.doe"))
                .thenReturn(Optional.of(entity));

        CreateTrainerDto upd = new CreateTrainerDto("Ann","Doe","CARDIO");
        TrainerDto dto = service.updateProfile("anna.doe","Pwd123", upd);

        assertThat(dto.firstName()).isEqualTo("Ann");
    }

    private Trainer stubTrainer(Long id) {
        User u = new User();
        u.setId(id + 100);
        u.setFirstName("A");
        u.setLastName("Doe");
        u.setUsername("anna.doe");
        u.setPassword("Pwd123");
        u.setActive(true);

        Trainer t = new Trainer();
        t.setId(id);
        t.setUser(u);
        t.setSpecialization(cardio);
        t.setTrainees(new HashSet<>());
        t.setTrainings(new HashSet<>());
        return t;
    }
}

