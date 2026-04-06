package com.epfcore.epfcore.security.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private Roles role;
    private String idRfid;
}
