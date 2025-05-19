package com.epam.gymapp.utils;

import com.epam.gymapp.repositories.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
public class CredentialGenerator {

    private final UserRepo userRepo;

    public CredentialGenerator(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public String randomPassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public String buildUniqueUsername(String first, String last) {
        String base = (first + "." + last).toLowerCase(Locale.ROOT);

        String candidate;
        do {
            String uuid8 = UUID.randomUUID()
                    .toString()
                    .substring(0, 8);
            candidate = base + "-" + uuid8;
        } while (userRepo.existsByUsername(candidate));

        return candidate;
    }
}

