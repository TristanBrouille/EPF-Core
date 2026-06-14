package com.epfcore.epfcore.documentFormulaire.dto;

public record DocumentFormulaireDTO(
        Long id,
        Long formulaireId,
        String documentType,
        String fileUrl,
        String fileName
) {}
