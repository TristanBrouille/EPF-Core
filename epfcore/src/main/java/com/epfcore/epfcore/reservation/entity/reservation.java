package com.epfcore.epfcore.reservation.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import com.epfcore.epfcore.salle.entity.salle;

@Entity
@Table(name = "reservations")

public class reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @ManyToOne
    @JoinColumn(name = "salle_id", nullable = false)
    private salle salle;

    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    
    private String nomReserveur;
    private Integer nbrPersonnes;
    private String description;

    @Enumerated(EnumType.STRING)
    private statusReservation status = statusReservation.EN_ATTENTE;

    public Long getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }

    public salle getSalle() {
        return salle;
    }

    public void setSalle(salle salle) {
        this.salle = salle;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public String getNomReserveur() {
        return nomReserveur;
    }

    public void setNomReserveur(String nomReserveur) {
        this.nomReserveur = nomReserveur;
    }

    public Integer getNbrPersonnes() {
        return nbrPersonnes;
    }

    public void setNbrPersonnes(Integer nbrPersonnes) {
        this.nbrPersonnes = nbrPersonnes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public statusReservation getStatus() {
        return status;
    }

    public void setStatus(statusReservation status) {
        this.status = status;
    }
}
