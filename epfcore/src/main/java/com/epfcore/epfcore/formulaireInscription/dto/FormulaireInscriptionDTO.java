package com.epfcore.epfcore.formulaireInscription.dto;

import com.epfcore.epfcore.security.exposition.UserExpose;

public record FormulaireInscriptionDTO(
        Long id,
        UserExpose user,
        String dernierDiplome,
        String etablissement,
        String niveauEtude,
        Integer anneeObtention,
        String programmeChoisi,
        String campusChoisi,
        String motivations,
        String avancement
) {}