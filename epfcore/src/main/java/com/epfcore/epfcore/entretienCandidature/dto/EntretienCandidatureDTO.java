package com.epfcore.epfcore.entretienCandidature.dto;

import com.epfcore.epfcore.entretienCandidature.entity.StatutEntretien;
import com.epfcore.epfcore.entretienCandidature.entity.TypeEntretien;
import com.epfcore.epfcore.security.exposition.UserExpose;

import java.time.LocalDateTime;

public record EntretienCandidatureDTO(
        Long id,
        Long formulaireId,
        UserExpose candidat,
        Long interviewerId,
        UserExpose interviewer,
        LocalDateTime dateHeure,
        String campusVille,
        String salle,
        String lienVisio,
        TypeEntretien typeEntretien,
        StatutEntretien statut,
        Double note,
        String commentaire,
        LocalDateTime dateCreation
) {}
