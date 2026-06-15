package com.epfcore.epfcore.formulaireInscription.repository;

import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormulaireInscriptionRepository extends JpaRepository<FormulaireInscription, Long> {
    Optional<FormulaireInscription> findByUserId(Long userId);
    List<FormulaireInscription> findBySoumisTrue();
}

