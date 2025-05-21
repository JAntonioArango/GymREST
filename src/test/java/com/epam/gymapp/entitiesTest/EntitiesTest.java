package com.epam.gymapp.entitiesTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.epam.gymapp.entities.Trainee;
import com.epam.gymapp.entities.Trainer;
import com.epam.gymapp.entities.TrainingType;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.repositories.TraineeRepo;
import com.epam.gymapp.repositories.TrainerRepo;
import com.epam.gymapp.repositories.TrainingTypeRepo;
import com.epam.gymapp.repositories.UserRepo;
import java.time.LocalDate;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class EntitiesTest {

  @Autowired UserRepo userRepo;
  @Autowired TraineeRepo traineeRepo;
  @Autowired TrainerRepo trainerRepo;
  @Autowired TrainingTypeRepo typeRepo;
  @Autowired TestEntityManager entityManager;

  /* ---------- 1. User basic persistence ---------- */
  @Test
  void user_is_persisted_and_retrieved_by_id() {
    User u = new User(null, "Sara", "Arango", "sara", "p@ss", true, null, null);
    User saved = userRepo.save(u);

    assertThat(saved.getId()).isNotNull();
    assertThat(userRepo.findById(saved.getId()))
        .hasValueSatisfying(found -> assertThat(found.getUsername()).isEqualTo("sara"));
  }

  /* ---------- 2. Cascade: Trainee owns User ---------- */
  @Test
  void trainee_cascades_user_on_save() {
    User u = new User(null, "Ana", "Lopez", "ana", "pwd", true, null, null);
    Trainee t =
        new Trainee(
            null, LocalDate.of(1990, 3, 4), "Main St 1", u, new HashSet<>(), new HashSet<>());
    Trainee saved = traineeRepo.save(t);

    // Trainee *and* User row should exist
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getUser().getId()).isNotNull();
    assertThat(userRepo.findByUsername("ana")).isPresent();
  }

  /* ---------- 3. Many-to-many Trainee ↔ Trainer ---------- */
  @Test
  void trainer_and_trainee_are_linked_through_join_table() {
    // --- given: create the FK data ---
    TrainingType cardio = typeRepo.save(new TrainingType(null, "Cardio", new HashSet<>()));

    User coachUser = userRepo.save(new User(null, "Coach", "K", "coach1", "pwd", true, null, null));
    Trainer coach =
        trainerRepo.save(new Trainer(null, cardio, coachUser, new HashSet<>(), new HashSet<>()));

    User traineeUser = userRepo.save(new User(null, "Bob", "B", "bob", "pwd", true, null, null));
    Trainee bob =
        new Trainee(
            null,
            LocalDate.of(2000, 1, 1),
            "Park Ave 5",
            traineeUser,
            new HashSet<>(),
            new HashSet<>());

    // --- keep both sides in sync ---
    bob.getTrainers().add(coach);
    coach.getTrainees().add(bob);

    // --- when: persist and immediately flush to the DB ---
    traineeRepo.saveAndFlush(bob);

    // force Hibernate to hit the DB on next access
    entityManager.flush();
    entityManager.clear();

    // --- then: reload and assert the bi-directional link ---
    Trainee reloadedTrainee = traineeRepo.findById(bob.getId()).orElseThrow();
    Trainer reloadedCoach = trainerRepo.findById(coach.getId()).orElseThrow();

    assertThat(reloadedTrainee.getTrainers()).containsExactly(reloadedCoach);
    assertThat(reloadedCoach.getTrainees()).containsExactly(reloadedTrainee);
  }

  /* ---------- 4. Constraint: username unique ---------- */
  @Test
  void saving_user_with_duplicate_username_throws() {
    userRepo.save(new User(null, "Mike", "X", "dup", "pwd", true, null, null));

    User dup = new User(null, "John", "Y", "dup", "pwd", true, null, null);

    assertThatThrownBy(() -> userRepo.saveAndFlush(dup))
        .isInstanceOf(DataIntegrityViolationException.class);
  }
}
