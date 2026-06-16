package com.epfcore.epfcore.user.dto;

import com.epfcore.epfcore.security.domain.Roles;

public record UserDto(Long id, String firstname, String lastname, Roles role) {
}
