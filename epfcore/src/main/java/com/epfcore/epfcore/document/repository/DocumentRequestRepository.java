package com.epfcore.epfcore.document.repository;

import org.springframework.stereotype.Repository;
import com.epfcore.epfcore.document.entity.DocumentRequest;
import com.epfcore.epfcore.document.entity.DocumentRequest.DocumentRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface DocumentRequestRepository extends JpaRepository<DocumentRequest, Integer> {

    List<DocumentRequest> findByStudentId(Integer studentId);
    List<DocumentRequest> findByStatus(DocumentRequestStatus status);
    List<DocumentRequest> findByStudentIdAndStatus(Integer studentId, DocumentRequestStatus status);
  
}
