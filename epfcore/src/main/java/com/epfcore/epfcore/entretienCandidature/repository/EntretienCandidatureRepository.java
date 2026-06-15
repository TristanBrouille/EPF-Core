package com.epfcore.epfcore.entretienCandidature.repository;

import com.epfcore.epfcore.entretienCandidature.entity.EntretienCandidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntretienCandidatureRepository extends JpaRepository<EntretienCandidature, Long> {
    List<EntretienCandidature> findByFormulaireId(Long formulaireId);
    List<EntretienCandidature> findByFormulaireUserId(Long userId);
}
