package com.epam.gymapp.credentialsTest.reposTest;

import com.epam.gymapp.entities.TrainingType;
import com.epam.gymapp.repositories.TrainingTypeRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TrainingTypeRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TrainingTypeRepo trainingTypeRepo;

    @Test
    void existsByName_ShouldReturnTrue_WhenNameExists() {
        // Given
        TrainingType trainingType = new TrainingType();
        trainingType.setName("Strength Training");
        entityManager.persist(trainingType);
        entityManager.flush();

        // When
        boolean exists = trainingTypeRepo.existsByName("Strength Training");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenNameDoesNotExist() {
        // When
        boolean exists = trainingTypeRepo.existsByName("Nonexistent Training");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void findByName_ShouldReturnTrainingType_WhenNameExists() {
        // Given
        TrainingType trainingType = new TrainingType();
        trainingType.setName("Yoga");
        entityManager.persist(trainingType);
        entityManager.flush();

        // When
        Optional<TrainingType> foundTrainingType = trainingTypeRepo.findByName("Yoga");

        // Then
        assertThat(foundTrainingType).isPresent();
        assertThat(foundTrainingType.get().getName()).isEqualTo("Yoga");
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenNameDoesNotExist() {
        // When
        Optional<TrainingType> foundTrainingType = trainingTypeRepo.findByName("Nonexistent");

        // Then
        assertThat(foundTrainingType).isEmpty();
    }
}