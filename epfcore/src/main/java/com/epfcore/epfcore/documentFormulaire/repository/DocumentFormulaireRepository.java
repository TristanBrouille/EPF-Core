package com.epfcore.epfcore.documentFormulaire.repository;

import com.epfcore.epfcore.documentFormulaire.entity.DocumentFormulaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentFormulaireRepository extends JpaRepository<DocumentFormulaire, Long> {
    List<DocumentFormulaire> findByFormulaireId(Long formulaireId);
    Optional<DocumentFormulaire> findByFormulaireIdAndDocumentType(Long formulaireId, String documentType);
}