package com.epfcore.epfcore.security.exposition;

import java.time.LocalDate;

public record UserExpose(String firstname, String lastname, String email, LocalDate birthday) {
}
