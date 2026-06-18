package com.epfcore.epfcore.grades.dao;

import com.epfcore.epfcore.grades.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteDao extends JpaRepository<Note, Long> {

    List<Note> findByEvaluationId(Long evaluationId);

    List<Note> findByEtudiantIdAndEvaluationCarnetId(Long etudiantId, Long carnetId);
}
