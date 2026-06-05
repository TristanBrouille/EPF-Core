package com.epfcore.epfcore.formulaireInscription.entity;

import com.epfcore.epfcore.campus.entity.Campus;
import com.epfcore.epfcore.security.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "formulaire_inscription")
public class FormulaireInscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "dernier_diplome")
    private String dernierDiplome;

    @Column(name = "etablissement")
    private String etablissement;

    @Column(name = "niveau_etude")
    private String niveauEtude;

    @Column(name = "annee_obtention")
    private Integer anneeObtention;

    @Column(name = "programme_choisi")
    private String programmeChoisi;

    @Column(columnDefinition = "TEXT")
    private String motivations;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "campus_id")
    private Campus campus;

    @Column(name = "soumis")
    private Boolean soumis;

    @Column(name = "genre")
    private String genre;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "annee_integration")
    private Integer anneeIntegration;

    @Column(name = "majeur")
    private String majeur;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }
}