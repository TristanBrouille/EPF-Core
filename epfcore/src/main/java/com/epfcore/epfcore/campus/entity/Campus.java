package com.epfcore.epfcore.campus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "campus")
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ville")
    private String ville;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "code_postal")
    private Integer codePostal;

    public Campus() {
    }

    public Campus(Long id, String ville, String adresse, Integer codePostal) {
        this.id = id;
        this.ville = ville;
        this.adresse = adresse;
        this.codePostal = codePostal;
    }

    
}