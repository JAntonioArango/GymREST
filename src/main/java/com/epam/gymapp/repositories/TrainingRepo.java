package com.epam.gymapp.repositories;

import com.epam.gymapp.api.dto.TraineeTrainingDto;
import com.epam.gymapp.api.dto.TrainerTrainingDto;
import com.epam.gymapp.api.dto.TrainingDto;
import com.epam.gymapp.entities.Training;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepo extends JpaRepository<Training, Long> {

  Page<Training> findByTraineeUserUsername(String traineeUsername, Pageable pageable);

  List<Training> findByTrainerUserUsername(String trainerUsername);

  // Get trainee’s trainings with optional filters
  @Query(
      """
           SELECT new com.epam.gymapp.api.dto.TrainingDto(
                     t.id,
                     t.trainee.user.username,
                     t.trainer.user.username,
                     t.trainer.user.firstName,
                     t.trainer.user.lastName,
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
  List<TrainingDto> findTraineeTrainingsJPQL(
      String traineeUsername,
      LocalDate fromDate,
      LocalDate toDate,
      String trainerName,
      String trainingType);

  // Get trainer’s trainings with optional filters
  @Query(
      """
           SELECT new com.epam.gymapp.api.dto.TrainingDto(
                     t.id,
                     t.trainee.user.username,
                     t.trainer.user.username,
                     t.trainer.user.firstName,
                     t.trainer.user.lastName,
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
  List<TrainingDto> findTrainerTrainingsJPQL(
      String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeName);

  @Query(
      """
   SELECT new com.epam.gymapp.api.dto.TraineeTrainingDto(
            t.trainingName,
            t.trainingDate,
            t.trainingType.name,
            t.trainingDuration,
            CONCAT(t.trainer.user.firstName,' ',t.trainer.user.lastName))
     FROM Training t
    WHERE t.trainee.user.username = :traineeUsername
      AND (:from IS NULL OR t.trainingDate >= :from)
      AND (:to   IS NULL OR t.trainingDate <= :to)
      AND (:trainerName IS NULL
           OR LOWER(CONCAT(t.trainer.user.firstName,' ',t.trainer.user.lastName))
              LIKE LOWER(CONCAT('%', :trainerName, '%')))
      AND (:trainingType IS NULL OR t.trainingType.name = :trainingType)
""")
  Page<TraineeTrainingDto> findTraineeTrainingRows(
      @Param("traineeUsername") String traineeUsername,
      @Param("from") LocalDate from,
      @Param("to") LocalDate to,
      @Param("trainerName") String trainerName,
      @Param("trainingType") String trainingType,
      Pageable pageable);

  @Query(
      """
   SELECT new com.epam.gymapp.api.dto.TrainerTrainingDto(
            t.trainingName,
            t.trainingDate,
            t.trainingType.name,
            t.trainingDuration,
            CONCAT(t.trainee.user.firstName,' ',t.trainee.user.lastName))
     FROM Training t
    WHERE t.trainer.user.username = :trainerUsername
      AND (:from IS NULL OR t.trainingDate >= :from)
      AND (:to   IS NULL OR t.trainingDate <= :to)
      AND (:traineeName IS NULL
           OR LOWER(CONCAT(t.trainee.user.firstName,' ',t.trainee.user.lastName))
              LIKE LOWER(CONCAT('%', :traineeName, '%')))
""")
  Page<TrainerTrainingDto> findTrainerTrainingRows(
      @Param("trainerUsername") String trainerUsername,
      @Param("from") LocalDate from,
      @Param("to") LocalDate to,
      @Param("traineeName") String traineeName,
      Pageable pageable);
}
