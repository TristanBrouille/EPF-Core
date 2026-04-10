package com.epfcore.epfcore.grades.entities;

import jakarta.persistence.Entity;

@Entity
public class Note {
    private float valeur_note;

    public Note(float valeur_note){
        this.valeur_note = valeur_note;
    }

    public float getValeurNote(){
        return valeur_note;
    }

    public void setValeurNote(float valeur_note){
        this.valeur_note = valeur_note;
    }
    
}
