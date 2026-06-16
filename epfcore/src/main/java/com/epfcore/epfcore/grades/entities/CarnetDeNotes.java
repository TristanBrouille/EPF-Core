package com.epfcore.epfcore.grades.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Carnet_de_notes")
public class CarnetDeNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 200)
    private String intitule;

    @Column(name = "annee_academique", nullable = false, length = 20)
    private String anneeAcademique;

    // BROUILLON | PUBLIE
    @Column(nullable = false, length = 20)
    private String statut = "BROUILLON";

    @Column(name = "moyenne_classe")
    private Float moyenneClasse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unite_enseignement_id")
    private UniteEnseignement uniteEnseignement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private Module module;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_publication")
    private LocalDateTime datePublication;

    @OneToMany(mappedBy = "carnet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();

    // ── Constructeurs ─────────────────────────────────────────────────────────
    public CarnetDeNotes() {}

    public CarnetDeNotes(String intitule, String anneeAcademique,
                         UniteEnseignement ue) {
        this.intitule          = intitule;
        this.anneeAcademique   = anneeAcademique;
        this.uniteEnseignement = ue;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public long              getId()                        { return id; }
    public String            getIntitule()                  { return intitule; }
    public void              setIntitule(String i)          { this.intitule = i; }
    public String            getAnneeAcademique()           { return anneeAcademique; }
    public void              setAnneeAcademique(String a)   { this.anneeAcademique = a; }
    public String            getStatut()                    { return statut; }
    public void              setStatut(String statut)       { this.statut = statut; }
    public Float             getMoyenneClasse()             { return moyenneClasse; }
    public void              setMoyenneClasse(Float m)      { this.moyenneClasse = m; }
    public UniteEnseignement getUniteEnseignement()         { return uniteEnseignement; }
    public void              setUniteEnseignement(UniteEnseignement ue){ this.uniteEnseignement = ue; }
    public Module            getModule()                    { return module; }
    public void              setModule(Module m)            { this.module = m; }
    public LocalDateTime     getDateCreation()              { return dateCreation; }
    public LocalDateTime     getDatePublication()           { return datePublication; }
    public void              setDatePublication(LocalDateTime d){ this.datePublication = d; }
    public List<Evaluation>  getEvaluations()               { return evaluations; }

    public boolean isPublie() { return "PUBLIE".equals(this.statut); }
}