package com.epfcore.epfcore.security.exposition;

import java.time.LocalDate;

public record UserWithCaptcha(
        String firstname,
        String lastname,
        String email,
        String password,
        LocalDate birthDate,
        String captchaToken
) {}