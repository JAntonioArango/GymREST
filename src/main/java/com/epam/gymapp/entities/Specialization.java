package com.epam.gymapp.entities;

import com.epam.gymapp.api.advice.ApiException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum Specialization {
  AEROBICS,
  CARDIO,
  STRENGTH,
  YOGA,
  PILATES,
  CROSSFIT,
  SPINNING,
  ZUMBA,
  BODYBUILDING,
  BALANCE,
  DANCING,
  BOXING,
  MEDITATION,
  OTHER;

  @JsonCreator
  public static Specialization of(String raw) {
    return Arrays.stream(values())
        .filter(v -> v.name().equalsIgnoreCase(raw))
        .findFirst()
        .orElseThrow(
            () ->
                ApiException.badRequest(
                    "Unknown specialization: '%s'. Allowed values: %s"
                        .formatted(raw, Arrays.toString(values()))));
  }

  @JsonValue
  public String json() {
    return name();
  }
}
