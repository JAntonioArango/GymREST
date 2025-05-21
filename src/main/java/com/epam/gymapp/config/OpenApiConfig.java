package com.epam.gymapp.config;

import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gymApiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gym Task REST API - Juan Antonio Arango")
                        .description("CRUD operations for Trainers, Trainees and Trainings")
                        .version("1.3.0")
                        .license(new License().name("Apache 2.0")))
                .addServersItem(new Server().url("http://localhost:8080"));
    }

    /** Limit docs to your controllers package (optional but keeps UI clean) */
    @Bean
    public GroupedOpenApi gymApi() {
        return GroupedOpenApi.builder()
                .group("gym")                     // ← you picked this name
                .packagesToScan("com.epam.gymapp.api.controllers") // MUST match your controllers’ package
                .pathsToMatch("/**")          // or "/**" if you prefer
                .build();
    }
}
