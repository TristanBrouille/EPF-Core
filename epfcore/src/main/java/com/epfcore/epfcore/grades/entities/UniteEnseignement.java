package com.epfcore.epfcore.grades.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UniteEnseignement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String code;
    // private ??? nature; (= optionnelle ou obligatoire)
    private String intitule;
    private Float credits_ECTS;
    private Float vol_horaire;
    private Float vol_horaire_CM;
    private Float vol_horaire_CMA;
    private Float vol_horaire_TD;
    private Float vol_horaire_TP;
    private Float vol_horaire_Projet;
    private Float vol_horaire_a_plannifier;
    private Float vol_horaire_non_plannifier;
    private Float coef;
    
    public UniteEnseignement(){}

    public UniteEnseignement(String intitule, 
                    Float credits_ECTS, Float vol_horaire, Float vol_horaire_CM,
                    Float vol_horaire_CMA, Float vol_horaire_TD, Float vol_horaire_TP,
                    Float vol_horaire_Projet, Float vol_horaire_a_plannifier,
                    Float vol_horaire_non_plannifier, Float coef){
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

    public Float getCredits_ECTS() {
        return credits_ECTS;
    }

    public void setCredits_ECTS(Float credits_ECTS) {
        this.credits_ECTS = credits_ECTS;
    }

    public Float getVolHoraire() {
        return vol_horaire;
    }

    public void setVolHoraire(Float vol_horaire) {
        this.vol_horaire = vol_horaire;
    }

    public Float getVolHoraireCM() {
        return vol_horaire_CM;
    }

    public void setVolHoraireCM(Float vol_horaire_CM) {
        this.vol_horaire_CM = vol_horaire_CM;
    }

    public Float getVolHoraireCMA() {
        return vol_horaire_CMA;
    }

    public void setVolHoraireCMA(Float vol_horaire_CMA) {
        this.vol_horaire_CMA = vol_horaire_CMA;
    }

    public Float getVolHoraireTD() {
        return vol_horaire_TD;
    }

    public void setVolHoraireTD(Float vol_horaire_TD) {
        this.vol_horaire_TD = vol_horaire_TD;
    }

    public Float getVolHoraireTP() {
        return vol_horaire_TP;
    }

    public void setVolHoraireTP(Float vol_horaire_TP) {
        this.vol_horaire_TP = vol_horaire_TP;
    }

    public Float getVolHoraireProjet() {
        return vol_horaire_Projet;
    }

    public void setVolHoraireProjet(Float vol_horaire_Projet) {
        this.vol_horaire_Projet = vol_horaire_Projet;
    }

    public Float getVolHoraireAPlannifier() {
        return vol_horaire_a_plannifier;
    }

    public void setVolHoraireAPlannifier(Float vol_horaire_a_plannifier) {
        this.vol_horaire_a_plannifier = vol_horaire_a_plannifier;
    }

    public Float getVolHoraireNonPlannifier() {
        return vol_horaire_non_plannifier;
    }

    public void setVolHoraireNonPlannifier(Float vol_horaire_non_plannifier) {
        this.vol_horaire_non_plannifier = vol_horaire_non_plannifier;
    }

    public Float getCoef() {
        return coef;
    }

    public void setCoef(Float coef) {
        this.coef = coef;
    }
}
