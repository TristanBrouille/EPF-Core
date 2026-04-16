package com.epfcore.epfcore.etudiant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.epfcore.epfcore.etudiant.entity.Etudiant;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    Optional<Etudiant> findByNumEtudiant(String numEtudiant);

    Optional<Etudiant> findByUserId(Long userId);

    @Query("select e from Etudiant e where e.user.email = :email")
    Optional<Etudiant> findByUserEmail(String email);

}
