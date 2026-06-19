package com.epfcore.epfcore.entretienCandidature.entity;

import com.epfcore.epfcore.campus.entity.Campus;
import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import com.epfcore.epfcore.security.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "entretien_candidature")
public class EntretienCandidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "formulaire_id", nullable = false)
    private FormulaireInscription formulaire;

    @ManyToOne
    @JoinColumn(name = "interviewer_id")
    private User interviewer;

    @Column(name = "date_heure")
    private LocalDateTime dateHeure;

    @ManyToOne
    @JoinColumn(name = "campus_id")
    private Campus campus;

    @Column(name = "salle")
    private String salle;

    @Column(name = "lien_visio")
    private String lienVisio;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_entretien", nullable = false)
    private TypeEntretien typeEntretien;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutEntretien statut;

    @Column(name = "note")
    private Double note;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutEntretien.PLANIFIE;
        }
    }
}
