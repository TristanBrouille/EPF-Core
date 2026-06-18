package com.epfcore.epfcore.grades.dto;

public class MoyenneRowDto {
    public Long   etudiantId;
    public String numero;
    public String nomComplet;
    public Float  moyenne;

    public MoyenneRowDto(Long etudiantId, String numero, String nomComplet, Float moyenne) {
        this.etudiantId  = etudiantId;
        this.numero      = numero;
        this.nomComplet  = nomComplet;
        this.moyenne     = moyenne;
    }
    
}
