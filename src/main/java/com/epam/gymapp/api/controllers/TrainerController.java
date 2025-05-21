package com.epam.gymapp.api.controllers;

import com.epam.gymapp.api.auth.AuthenticatedUser;
import com.epam.gymapp.api.dto.*;
import com.epam.gymapp.entities.User;
import com.epam.gymapp.services.TrainerService;
import com.epam.gymapp.utils.ApiListWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trainer")
@RequiredArgsConstructor
@Tag(name = "Trainer")
public class TrainerController {

    private final TrainerService trainerService;

    /* --------- CREATE --------- */
    @PostMapping("/register")
    @Operation(summary = "Trainer Registration (2)")
    public ResponseEntity<TrainerRegistrationDto> create(
            @Valid @RequestBody CreateTrainerDto dto) {

        TrainerRegistrationDto created =  trainerService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /* ---------- READ ---------- */
    @GetMapping("/{username}")
    @Operation(summary = "Get trainer profile (8)")
    public TrainerProfileDto getProfile(
            @PathVariable String username,
            @AuthenticatedUser User caller) {

        return trainerService.findProfile(username);
    }


    @GetMapping
    @Operation(summary = "List trainers - paged")
    public ApiListWrapper<TrainerDto> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticatedUser User caller) {

        Page<TrainerDto> p = trainerService.list(PageRequest.of(page, size));
        return new ApiListWrapper<>(p.getContent());   // simple wrapper
    }

    /* ------ UPDATE (PUT) ------ */
    @PutMapping("/{username}")
    @Operation(summary = "Update trainer profile (9)")
    public TrainerProfileDto updateProfile(
            @PathVariable String username,
            @Valid @RequestBody UpdateTrainerDto body,
            @AuthenticatedUser User caller) {

        return trainerService.updateProfile(username, body);
    }

    /* -- ACTIVATE/DEACTIVATE (PATCH) -- */
    @PatchMapping("/{username}/active")
    @Operation(summary = "Activate/De-Activate Trainer (16)")
    public ResponseEntity<Void> setActive(
            @PathVariable String username,
            @RequestParam boolean active,
            @AuthenticatedUser User caller) {

        trainerService.setActive(username, active);
        return ResponseEntity.ok().build();
    }


}
