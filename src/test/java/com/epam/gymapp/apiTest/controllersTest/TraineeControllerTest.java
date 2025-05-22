package com.epam.gymapp.apiTest.controllersTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.epam.gymapp.api.controllers.TraineeController;
import com.epam.gymapp.api.dto.*;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.services.TraineeService;
import com.epam.gymapp.services.TrainerService;
import com.epam.gymapp.utils.ApiListWrapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

  @Mock private TraineeService traineeService;
  @Mock private TrainerService trainerService;

  @InjectMocks private TraineeController controller;

  private User caller;

  @BeforeEach
  void setUp() {
    caller = new User();
    caller.setUsername("admin");
  }

  /* -------- CREATE -------- */
  @Test
  void create_shouldReturn201WithCredentials() {
    CreateTraineeDto in =
        new CreateTraineeDto("John", "Doe", LocalDate.of(1990, 1, 1), "Address 1");
    TraineeRegistrationDto out = new TraineeRegistrationDto("john.doe-1234", "pass");
    when(traineeService.register(in)).thenReturn(out);

    ResponseEntity<TraineeRegistrationDto> resp = controller.create(in);

    assertEquals(HttpStatus.CREATED, resp.getStatusCode());
    assertEquals(out, resp.getBody());
    verify(traineeService).register(in);
  }

  /* -------- DELETE -------- */
  @Test
  void delete_shouldInvokeServiceAndReturnOK() {
    ResponseEntity<Void> resp = controller.delete("john", caller);
    assertEquals(HttpStatus.OK, resp.getStatusCode());
    verify(traineeService).deleteByUsername("john");
  }

  /* --- ACTIVATE / DEACTIVATE --- */
  @Test
  void setActive_shouldCallServiceAndReturnOK() {
    ResponseEntity<Void> resp = controller.setActive("john", true, caller);
    assertEquals(HttpStatus.OK, resp.getStatusCode());
    verify(traineeService).setActive("john", true);
  }

  /* ---- REPLACE TRAINERS ---- */
  @Test
  void replaceTrainers_shouldReturnWrappedList() {
    List<String> trainerUsernames = List.of("sara.maria");
    UpdateTraineeTrainersDto body = new UpdateTraineeTrainersDto(trainerUsernames);
    List<TrainerShortDto> trainers =
        List.of(new TrainerShortDto("sara.maria", "Sara", "Maria", "BOXING"));
    when(traineeService.replaceTrainers("john", trainerUsernames)).thenReturn(trainers);

    ApiListWrapper<TrainerShortDto> wrapper = controller.replaceTrainers("john", body, caller);

    assertEquals(trainers, wrapper.items());
    verify(traineeService).replaceTrainers("john", trainerUsernames);
  }

  /* -------- LIST -------- */
  @Test
  void list_shouldReturnPageContentWrapped() {
    List<TraineeDto> dtos =
        List.of(new TraineeDto("john", "John", "Doe", LocalDate.of(1990, 1, 1), "Address", true));
    when(traineeService.list(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(dtos));

    ApiListWrapper<TraineeDto> wrapper = controller.list(0, 20, caller);

    assertEquals(dtos, wrapper.items());
  }
}
