package com.epam.gymapp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenDto(@Schema(example = "xxxxx.yyyyy.zzzzz") String token) {}
