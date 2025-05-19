package com.epam.gymapp.repositories;

import com.epam.gymapp.dto.TrainingDto;
import com.epam.gymapp.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepo extends JpaRepository<Training, Long> {

    List<Training> findByTraineeUserUsername(String traineeUsername);
    List<Training> findByTrainerUserUsername(String trainerUsername);

    // Get trainee’s trainings with optional filters
    @Query("""
           SELECT new com.epam.gymapp.dto.TrainingDto(
                     t.id,
                     t.trainee.user.username,
                     t.trainer.user.username,
                     t.trainingType.name,
                     t.trainingName,
                     t.trainingDate,
                     t.trainingDuration)
           FROM  Training t
           WHERE t.trainee.user.username = :traineeUsername
             AND (:fromDate  IS NULL OR t.trainingDate >= :fromDate)
             AND (:toDate    IS NULL OR t.trainingDate <= :toDate)
             AND (COALESCE(:trainerName,'') = '' OR
                  LOWER(CONCAT(t.trainer.user.firstName,' ',t.trainer.user.lastName))
                      LIKE LOWER(CONCAT('%', :trainerName, '%')))
             AND (COALESCE(:trainingType,'') = '' OR
                  t.trainingType.name = :trainingType)
           """)
    List<TrainingDto> findTraineeTrainingsJPQL(String traineeUsername,
                                               LocalDate fromDate,
                                               LocalDate toDate,
                                               String trainerName,
                                               String trainingType);

    //Get trainer’s trainings with optional filters
    @Query("""
           SELECT new com.epam.gymapp.dto.TrainingDto(
                     t.id,
                     t.trainee.user.username,
                     t.trainer.user.username,
                     t.trainingType.name,
                     t.trainingName,
                     t.trainingDate,
                     t.trainingDuration)
           FROM  Training t
           WHERE t.trainer.user.username = :trainerUsername
             AND (:fromDate IS NULL OR t.trainingDate >= :fromDate)
             AND (:toDate   IS NULL OR t.trainingDate <= :toDate)
             AND (COALESCE(:traineeName,'') = '' OR
                  LOWER(CONCAT(t.trainee.user.firstName,' ',t.trainee.user.lastName))
                      LIKE LOWER(CONCAT('%', :traineeName, '%')))
           """)
    List<TrainingDto> findTrainerTrainingsJPQL(String trainerUsername,
                                               LocalDate fromDate,
                                               LocalDate toDate,
                                               String traineeName);
}
