package com.epfcore.epfcore.grades.entities;

import java.util.ArrayList;

import jakarta.persistence.Entity;

@Entity
public class Bulletin {
    private ArrayList<UniteEnseignement> unites_enseignement;
    private ArrayList<Module> modules;
    // private Etudiant etudiant;


    // public enum Combinaison {
    //     CARTE_HAUTE, PAIRE, DOUBLE_PAIRE, BRELAN, CARRE, AUTRE, SUITE, COULEUR, FULL, QUINTE_FLUSH, QUINTE_FLUSH_ROYALE
    // }

    
}
