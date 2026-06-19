package com.epfcore.epfcore.grades.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// Faire une liste de modules, pour ajouter un module, add() et pour récupérer les infos du module : get().

@Entity
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String code;
    // private ??? nature; (= optionnelle ou obligatoire)
    private String intitule;
    private float credits_ECTS;
    private float vol_horaire;
    private float vol_horaire_CM;
    private float vol_horaire_CMA;
    private float vol_horaire_TD;
    private float vol_horaire_TP;
    private float vol_horaire_Projet;
    private float vol_horaire_a_plannifier;
    private float vol_horaire_non_plannifier;
    private float coef;
    
    public Module(){}

    public Module(String intitule, 
                    float credits_ECTS, float vol_horaire, float vol_horaire_CM,
                    float vol_horaire_CMA, float vol_horaire_TD, float vol_horaire_TP,
                    float vol_horaire_Projet, float vol_horaire_a_plannifier,
                    float vol_horaire_non_plannifier, float coef){
        this.intitule = intitule;
        this.credits_ECTS = credits_ECTS;
        this.vol_horaire = vol_horaire;
        this.vol_horaire_CM = vol_horaire_CM;
        this.vol_horaire_CMA = vol_horaire_CMA;
        this.vol_horaire_TD = vol_horaire_TD;
        this.vol_horaire_TP = vol_horaire_TP;
        this.vol_horaire_Projet = vol_horaire_Projet;
        this.vol_horaire_a_plannifier = vol_horaire_a_plannifier;
        this.vol_horaire_non_plannifier = vol_horaire_non_plannifier;
        this.coef = coef;
    }

    public long getId() {
        return id;
    }

    // public void setId(long id) {
    //     this.id = id;
    // }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // public long getNature() {
    //     return nature;
    // }

    // public void setNature(long nature) {
    //     this.nature = nature;
    // }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public float getCredits_ECTS() {
        return credits_ECTS;
    }

    public void setCredits_ECTS(float credits_ECTS) {
        this.credits_ECTS = credits_ECTS;
    }

    public float getVolHoraire() {
        return vol_horaire;
    }

    public void setVolHoraire(float vol_horaire) {
        this.vol_horaire = vol_horaire;
    }

    public float getVolHoraireCM() {
        return vol_horaire_CM;
    }

    public void setVolHoraireCM(float vol_horaire_CM) {
        this.vol_horaire_CM = vol_horaire_CM;
    }

    public float getVolHoraireCMA() {
        return vol_horaire_CMA;
    }

    public void setVolHoraireCMA(float vol_horaire_CMA) {
        this.vol_horaire_CMA = vol_horaire_CMA;
    }

    public float getVolHoraireTD() {
        return vol_horaire_TD;
    }

    public void setVolHoraireTD(float vol_horaire_TD) {
        this.vol_horaire_TD = vol_horaire_TD;
    }

    public float getVolHoraireTP() {
        return vol_horaire_TP;
    }

    public void setVolHoraireTP(float vol_horaire_TP) {
        this.vol_horaire_TP = vol_horaire_TP;
    }

    public float getVolHoraireProjet() {
        return vol_horaire_Projet;
    }

    public void setVolHoraireProjet(float vol_horaire_Projet) {
        this.vol_horaire_Projet = vol_horaire_Projet;
    }

    public float getVolHoraireAPlannifier() {
        return vol_horaire_a_plannifier;
    }

    public void setVolHoraireAPlannifier(float vol_horaire_a_plannifier) {
        this.vol_horaire_a_plannifier = vol_horaire_a_plannifier;
    }

    public float getVolHoraireNonPlannifier() {
        return vol_horaire_non_plannifier;
    }

    public void setVolHoraireNonPlannifier(float vol_horaire_non_plannifier) {
        this.vol_horaire_non_plannifier = vol_horaire_non_plannifier;
    }

    public float getCoef() {
        return coef;
    }

    public void setCoef(float coef) {
        this.coef = coef;
    }
}
