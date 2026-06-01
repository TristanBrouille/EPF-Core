package com.epfcore.epfcore.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.epfcore.epfcore.document.entity.Document;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findByStudentId(Integer studentId);
 
    Optional<Document> findByRequestId(Integer requestId);
 
    List<Document> findByStudentIdAndAcademicYear(Integer studentId, String academicYear);

    
}
