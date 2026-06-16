// package com.epfcore.epfcore.grades.entities;

// import jakarta.persistence.Entity;

// @Entity
// public class Note {
//     private float valeur_note;

//     public Note(float valeur_note){
//         this.valeur_note = valeur_note;
//     }

//     public float getValeurNote(){
//         return valeur_note;
//     }

//     public void setValeurNote(float valeur_note){
//         this.valeur_note = valeur_note;
//     }
    
// }


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

    @Column(name = "valeur_note")
    private Float valeur_note;   // Float (nullable) : null = absent

    @Column(name = "absent", nullable = false)
    private boolean absent = false;

    @Column(name = "commentaire", length = 500)
    private String commentaire;

    @Column(name = "source", nullable = false, length = 20)
    private String source = "MANUELLE";   // MANUELLE | IMPORT_CSV

    @Column(name = "date_saisie", nullable = false)
    private LocalDateTime dateSaisie = LocalDateTime.now();

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    // ── Constructeurs ─────────────────────────────────────────────────────────
    public Note() {}

    public Note(Float valeur_note, Etudiant etudiant, Evaluation evaluation, String source) {
        this.valeur_note = valeur_note;
        this.etudiant    = etudiant;
        this.evaluation  = evaluation;
        this.source      = source;
        this.absent      = (valeur_note == null);
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public long getId()                        { return id; }
    public Float getValeurNote()               { return valeur_note; }
    public void  setValeurNote(Float v)        { this.valeur_note = v; this.absent = (v == null); }
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