package com.epfcore.epfcore.reservation.controller;

import com.epfcore.epfcore.reservation.entity.reservation;
import com.epfcore.epfcore.reservation.entity.statusReservation;
import com.epfcore.epfcore.reservation.repository.reservationRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/reservation")
public class reservationController {

    private final reservationRepository reservationRepository;

    public reservationController(reservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public List<reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @PostMapping("/update/{id}") 
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody reservation reservation) {
    reservation.setIdReservation(id); 
    reservation sauvegarde = reservationRepository.save(reservation);
    return ResponseEntity.ok(sauvegarde);
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody reservation reservation) {
        try {
            validerHoraires(reservation.getDate(), reservation.getHeureDebut(), reservation.getHeureFin());

            reservation.setStatus(statusReservation.EN_ATTENTE);
            
            reservation sauvegarde = reservationRepository.save(reservation);
            return ResponseEntity.ok(sauvegarde);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage());
        }
    }

    private void validerHoraires(LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        if (date == null || heureDebut == null || heureFin == null) {
            throw new IllegalArgumentException("La date, l'heure de début et l'heure de fin sont obligatoires.");
        }

        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Les réservations sont interdites le dimanche.");
        }

        LocalTime limiteDebut = LocalTime.of(8, 0);
        LocalTime limiteFin = LocalTime.of(21, 0);

        if (heureDebut.isBefore(limiteDebut)) {
            throw new IllegalArgumentException("L'heure de début doit être après 8h00.");
        }
        if (heureFin.isAfter(limiteFin)) {
            throw new IllegalArgumentException("L'heure de fin doit être avant 21h00.");
        }
        if (heureFin.isBefore(heureDebut) || heureFin.equals(heureDebut)) {
            throw new IllegalArgumentException("L'heure de fin doit être après l'heure de début.");
        }
    }
}