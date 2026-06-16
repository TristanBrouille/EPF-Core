package com.epfcore.epfcore.grades.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Etudiant")
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;   // numéro étudiant

    @Column(length = 100)
    private String programme;

    @Column(name = "annee_academique", length = 20)
    private String anneeAcademique;

    // ── Constructeurs ─────────────────────────────────────────────────────────
    public Etudiant() {}

    public Etudiant(String nom, String prenom, String email, String numero,
                    String programme, String anneeAcademique) {
        this.nom              = nom;
        this.prenom           = prenom;
        this.email            = email;
        this.numero           = numero;
        this.programme        = programme;
        this.anneeAcademique  = anneeAcademique;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public long   getId()                           { return id; }
    public String getNom()                          { return nom; }
    public void   setNom(String nom)                { this.nom = nom; }
    public String getPrenom()                       { return prenom; }
    public void   setPrenom(String prenom)          { this.prenom = prenom; }
    public String getEmail()                        { return email; }
    public void   setEmail(String email)            { this.email = email; }
    public String getNumero()                       { return numero; }
    public void   setNumero(String numero)          { this.numero = numero; }
    public String getProgramme()                    { return programme; }
    public void   setProgramme(String programme)    { this.programme = programme; }
    public String getAnneeAcademique()              { return anneeAcademique; }
    public void   setAnneeAcademique(String a)      { this.anneeAcademique = a; }

    public String getNomComplet()                   { return prenom + " " + nom; }
}