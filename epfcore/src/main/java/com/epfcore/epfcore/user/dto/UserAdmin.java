package com.epfcore.epfcore.user.dto;

import com.epfcore.epfcore.security.domain.Roles;

import java.time.LocalDate;

public record UserAdmin(Long id, String firstname, String lastname, String email, String password, LocalDate birthDate, Roles role, String idRfid) {
}
