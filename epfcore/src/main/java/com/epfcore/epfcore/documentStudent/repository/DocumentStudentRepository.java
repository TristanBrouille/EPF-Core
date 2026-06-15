package com.epfcore.epfcore.documentStudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.epfcore.epfcore.documentStudent.entity.DocumentStudent;
import java.util.List;

@Repository
public interface DocumentStudentRepository extends JpaRepository<DocumentStudent, Integer> {

    List<DocumentStudent> findByUserId(Integer userId);
    List<DocumentStudent> findByStatus(DocumentStudent status);
    List<DocumentStudent> findByUserIdAndStatus(Integer userId, DocumentStudent status);
}
