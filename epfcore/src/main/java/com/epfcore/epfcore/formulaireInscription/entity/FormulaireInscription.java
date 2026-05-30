package com.epfcore.epfcore.formulaireInscription.entity;

import com.epfcore.epfcore.security.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "campus_choisi")
    private String campusChoisi;

    @Column(columnDefinition = "TEXT")
    private String motivations;

    @Column(name = "avancement")
    private String avancement = "en_cours";

}
