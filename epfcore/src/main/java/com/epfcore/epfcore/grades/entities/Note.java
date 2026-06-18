package com.epfcore.epfcore.grades.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Note",
       uniqueConstraints = @UniqueConstraint(columnNames = {"etudiant_id", "evaluation_id"}))
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

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    public Note() {}

    public Note(Float valeur_note, Etudiant etudiant, Evaluation evaluation, String source) {
        this.valeurNote = valeur_note;
        this.etudiant    = etudiant;
        this.evaluation  = evaluation;
        this.source      = source;
        this.absent      = (valeur_note == null);
    }

    public long getId()                        { return id; }
    public Float getValeurNote()               { return valeurNote; }
    public void  setValeurNote(Float v)        { this.valeurNote = v; this.absent = (v == null); }
    public boolean isAbsent()                  { return absent; }
    public void  setAbsent(boolean absent)     { this.absent = absent; }
    public String getCommentaire()             { return commentaire; }
    public void  setCommentaire(String c)      { this.commentaire = c; }
    public String getSource()                  { return source; }
    public void  setSource(String source)      { this.source = source; }
    public LocalDateTime getDateSaisie()       { return dateSaisie; }
    public void  setDateSaisie(LocalDateTime d){ this.dateSaisie = d; }
    public Etudiant getEtudiant()              { return etudiant; }
    public void  setEtudiant(Etudiant e)       { this.etudiant = e; }
    public Evaluation getEvaluation()          { return evaluation; }
    public void  setEvaluation(Evaluation e)   { this.evaluation = e; }
}