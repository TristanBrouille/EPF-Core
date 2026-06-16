package com.epfcore.epfcore.grades.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Evaluation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 200)
    private String intitule;

    // DS | TP | PROJET | RATTRAPAGE | EXAMEN | AUTRE
    @Column(nullable = false, length = 50)
    private String type = "DS";

    @Column(name = "date_eval")
    private LocalDate dateEval;

    @Column(nullable = false)
    private float coef = 1f;

    @Column(name = "note_max", nullable = false)
    private float noteMax = 20f;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carnet_id", nullable = false)
    private CarnetDeNotes carnet;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    // ── Constructeurs ─────────────────────────────────────────────────────────
    public Evaluation() {}

    public Evaluation(String intitule, String type, float coef, float noteMax,
                      CarnetDeNotes carnet) {
        this.intitule = intitule;
        this.type     = type;
        this.coef     = coef;
        this.noteMax  = noteMax;
        this.carnet   = carnet;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public long            getId()                      { return id; }
    public String          getIntitule()                { return intitule; }
    public void            setIntitule(String i)        { this.intitule = i; }
    public String          getType()                    { return type; }
    public void            setType(String type)         { this.type = type; }
    public LocalDate       getDateEval()                { return dateEval; }
    public void            setDateEval(LocalDate d)     { this.dateEval = d; }
    public float           getCoef()                    { return coef; }
    public void            setCoef(float coef)          { this.coef = coef; }
    public float           getNoteMax()                 { return noteMax; }
    public void            setNoteMax(float noteMax)    { this.noteMax = noteMax; }
    public CarnetDeNotes   getCarnet()                  { return carnet; }
    public void            setCarnet(CarnetDeNotes c)   { this.carnet = c; }
    public List<Note>      getNotes()                   { return notes; }
}