package com.epfcore.epfcore.salle;
import jakarta.persistence.*;
import com.epfcore.epfcore.campus.entity.Campus;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

enum TypeSalle{Amphi, Autres, Exterieur, Labo, Projet, TD, Techlab}


@Entity
@Table(name = "salles")
public class salle {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Indique l'auto-incrément (PRIMARY KEY AUTO_INCREMENT)
    private Integer id;
    
    private String nomSalle;
    private int capacite;
    private String equipement;
    
    @ManyToOne
    @JoinColumn(name = "campus_id")
    private Campus campus;
    
    @Enumerated(EnumType.STRING)
    private TypeSalle typeSalle;

    public salle() {}

    public salle(Integer id, String nomSalle, int capacite, String equipement, Campus campus, TypeSalle typeSalle) {
        this.id = id;
        this.nomSalle = nomSalle;
        this.capacite = capacite;
        this.equipement = equipement;
        this.campus = campus;
        this.typeSalle = typeSalle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public void setNomSalle(String nomSalle) {
        this.nomSalle = nomSalle;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getEquipement() {
        return equipement;
    }

    public void setEquipement(String equipement) {
        this.equipement = equipement;
    }

    public Campus getCampus() { return campus; }
    public void setCampus(Campus campus) { this.campus = campus; }

    public TypeSalle getTypeSalle() {
        return typeSalle;
    }

    public void setTypeSalle(TypeSalle typeSalle) {
        this.typeSalle = typeSalle;
    }
    
}