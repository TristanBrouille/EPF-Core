package com.epfcore.epfcore.salle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epfcore.epfcore.salle.entity.salle;

@Repository
public interface salleRepository extends JpaRepository<salle, Integer> {
}