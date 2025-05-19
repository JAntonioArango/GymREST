package com.epam.gymapp.training;

import com.epam.gymapp.dto.CreateTrainingDto;
import com.epam.gymapp.dto.TrainingDto;
import com.epam.gymapp.entities.*;
import com.epam.gymapp.repositories.*;
import com.epam.gymapp.services.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock TrainingRepo  trainingRepo;
    @Mock TraineeRepo   traineeRepo;
    @Mock TrainerRepo   trainerRepo;
    @Mock TrainingTypeRepo typeRepo;

    @InjectMocks TrainingService service;

    @Test
    void addTraining_persistsEntity() {
        Trainee trainee = stubTrainee(1L);
        Trainer trainer = stubTrainer(2L);
        TrainingType type = stubType();

        when(traineeRepo.findByUserUsername("john")).thenReturn(Optional.of(trainee));
        when(trainerRepo.findByUserUsername("anna")).thenReturn(Optional.of(trainer));
        when(typeRepo.findByName("CARDIO")).thenReturn(Optional.of(type));
        when(trainingRepo.save(any(Training.class))).thenAnswer(i -> i.getArgument(0));

        CreateTrainingDto dto = new CreateTrainingDto(
                "john","anna","CARDIO",
                "Run", LocalDate.now(), 45);

        TrainingDto out = service.addTraining(dto);

        assertThat(out.trainingType()).isEqualTo("CARDIO");
        verify(trainingRepo).save(any(Training.class));
    }

    @Test
    void listByTrainer_filtersByTraineeName() {
        when(trainingRepo.findAll((Specification<Training>) any()))
                .thenReturn(List.of(stubTraining()));

        List<TrainingDto> list =
                service.listByTrainer("anna", null, null, "John");

        assertThat(list).hasSize(1);
    }

    private Training stubTraining() {
        Training tr = new Training();
        tr.setId(77L);
        tr.setTrainingName("Run");
        tr.setTrainingDate(LocalDate.now());
        tr.setTrainingDuration(30);

        tr.setTrainingType(stubType());
        tr.setTrainer(stubTrainer(2L));
        tr.setTrainee(stubTrainee(1L));
        return tr;
    }

    private TrainingType stubType() {
        TrainingType t = new TrainingType();
        t.setId(9L);
        t.setName("CARDIO");
        return t;
    }

    private Trainee stubTrainee(Long id) {
        User u = new User();
        u.setId(id + 100);
        u.setUsername("john");
        u.setFirstName("John");
        u.setLastName("Smith");
        u.setPassword("pwd");

        Trainee t = new Trainee();
        t.setId(id);
        t.setUser(u);
        t.setTrainings(new HashSet<>());
        return t;
    }

    private Trainer stubTrainer(Long id) {
        User u = new User();
        u.setId(id + 200);
        u.setUsername("anna");
        u.setFirstName("Anna");
        u.setLastName("Doe");
        u.setPassword("pwd");

        Trainer tr = new Trainer();
        tr.setId(id);
        tr.setUser(u);
        tr.setSpecialization(stubType());
        tr.setTrainees(new HashSet<>());
        tr.setTrainings(new HashSet<>());
        return tr;
    }
}
