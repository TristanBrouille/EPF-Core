package com.epfcore.epfcore.formulaireInscription.dto;

import com.epfcore.epfcore.formulaireInscription.entity.DecisionAdmission;
import com.epfcore.epfcore.security.exposition.UserExpose;
import java.time.LocalDateTime;

public record FormulaireInscriptionDTO(
        Long id,
        UserExpose user,
        String dernierDiplome,
        String etablissement,
        String niveauEtude,
        Integer anneeObtention,
        String programmeChoisi,
        LocalDateTime dateSoumission,
        String campusVille,
        Boolean soumis,
        String genre,
        String telephone,
        String nationalite,
        String adresse,
        Integer anneeIntegration,
        String majeur,
        DecisionAdmission decisionAdmission
) {}