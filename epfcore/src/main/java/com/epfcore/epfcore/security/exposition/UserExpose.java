package com.epfcore.epfcore.security.exposition;

import com.epfcore.epfcore.security.domain.Roles;

import java.time.LocalDate;

public record UserExpose(String firstname, String lastname, String email, LocalDate birthday, Roles role) {
}
