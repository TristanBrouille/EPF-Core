package com.epfcore.epfcore.campus.repository;

import com.epfcore.epfcore.campus.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {
    Optional<Campus> findByVille(String ville);
}