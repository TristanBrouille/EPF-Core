package com.epfcore.epfcore.grades.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Note",
       uniqueConstraints = @UniqueConstraint(columnNames = {"etudiant_id", "evaluation_id"}))
@Getter
@Setter
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "valeur_Note")
    private Float valeurNote;

    @Column(name = "absent", nullable = false)
    private boolean absent = false;

    @Column(name = "commentaire", length = 500)
    private String commentaire;

    @Column(name = "source", nullable = false, length = 20)
    private String source = "MANUELLE";

    @Column(name = "date_saisie", nullable = false)
    private LocalDateTime dateSaisie = LocalDateTime.now();

    @Column(name = "etudiant_id", nullable = false)
    private Long etudiantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    public Note() {}

    public Note(Float valeur_note, Long etudiantId, Evaluation evaluation, String source) {
        this.valeurNote  = valeur_note;
        this.etudiantId  = etudiantId;
        this.evaluation  = evaluation;
        this.source      = source;
        this.absent      = (valeur_note == null);
    }

}