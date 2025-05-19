package com.epam.gymapp.services;

import com.epam.gymapp.dto.*;
import com.epam.gymapp.entities.*;
import com.epam.gymapp.exceptions.*;
import com.epam.gymapp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepo     trainingRepo;
    private final TraineeRepo      traineeRepo;
    private final TrainerRepo      trainerRepo;
    private final TrainingTypeRepo typeRepo;

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    public TrainingDto addTraining(CreateTrainingDto dto) {

        Trainee trainee = traineeRepo.findByUserUsername(dto.traineeUsername())
                .orElseThrow(() -> new TraineeNotFoundException(dto.traineeUsername()));

        Trainer trainer = trainerRepo.findByUserUsername(dto.trainerUsername())
                .orElseThrow(() -> new TrainerNotFoundException(dto.trainerUsername()));

        TrainingType type = typeRepo.findByName(dto.trainingTypeCode())
                .orElseThrow(() -> new TrainingNotFoundException(dto.trainingTypeCode()));

        Training tr = new Training(
                null, trainee, trainer, type,
                dto.name(), dto.date(), dto.duration());

        trainingRepo.save(tr);
        return toDto(tr);
    }

    public List<TrainingDto> listByTrainee(String traineeUsername) {
        return trainingRepo.findByTraineeUserUsername(traineeUsername)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<TrainingDto> listByTrainer(String trainerUsername) {
        return trainingRepo.findByTrainerUserUsername(trainerUsername)
                .stream()
                .map(this::toDto)
                .toList();
    }

    //Get trainee’s trainings by optional filters (date range, trainer name, type)
    public List<TrainingDto> listByTrainee(String traineeUsername,
                                           TrainingFilter filter) {

        var f = (filter == null) ? TrainingFilter.EMPTY : filter;

        return trainingRepo.findTraineeTrainingsJPQL(
                traineeUsername,
                f.fromDate(),
                f.toDate(),
                f.trainerNameOrNull(),
                f.trainingTypeOrNull()
        );
    }

    public record TrainingFilter(LocalDate fromDate,
                                 LocalDate toDate,
                                 String trainerName,
                                 String trainingType) {

        // Static “empty” filter so we never have to check null in the service
        public static final TrainingFilter EMPTY = new TrainingFilter(
                null,
                null,
                null,
                null);

        public String trainerNameOrNull()  { return blankToNull(trainerName);  }
        public String trainingTypeOrNull() { return blankToNull(trainingType); }

        private static String blankToNull(String s) {
            return (s == null || s.isBlank()) ? null : s;
        }
    }

    //Get trainer’s trainings by optional filters (date range, trainee name)
    public List<TrainingDto> listByTrainer(String trainerUsername,
                                           LocalDate fromDate,
                                           LocalDate toDate,
                                           String traineeName) {
        return trainingRepo.findTrainerTrainingsJPQL(
                trainerUsername,
                fromDate,
                toDate,
                blankToNull(traineeName)
        );
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    private TrainingDto toDto(Training t) {
        return new TrainingDto(
                t.getId(),
                t.getTrainee().getUser().getUsername(),
                t.getTrainer().getUser().getUsername(),
                t.getTrainingType().getName(),
                t.getTrainingName(),
                t.getTrainingDate(),
                t.getTrainingDuration()
        );
    }
}
