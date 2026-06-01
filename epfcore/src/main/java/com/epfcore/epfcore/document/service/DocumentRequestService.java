package com.epfcore.epfcore.document.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.epfcore.epfcore.document.entity.DocumentRequest;
import com.epfcore.epfcore.document.entity.DocumentRequest.DocumentRequestStatus;
import com.epfcore.epfcore.document.repository.DocumentRequestRepository;

@Service
public class DocumentRequestService {
    
    private final DocumentRequestRepository documentRequestRepository;
 
    public DocumentRequestService(DocumentRequestRepository documentRequestRepository) {
        this.documentRequestRepository = documentRequestRepository;
    }
 
    public List<DocumentRequest> getAll() {
        return documentRequestRepository.findAll();
    }
 
    public DocumentRequest getById(Integer id) {
        return documentRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DocumentRequest not found with id: " + id));
    }
 
    public List<DocumentRequest> getByStudentId(Integer studentId) {
        return documentRequestRepository.findByStudentId(studentId);
    }
 
    public List<DocumentRequest> getByStatus(DocumentRequestStatus status) {
        return documentRequestRepository.findByStatus(status);
    }
 
    public List<DocumentRequest> getByStudentIdAndStatus(Integer studentId, DocumentRequestStatus status) {
        return documentRequestRepository.findByStudentIdAndStatus(studentId, status);
    }
 
    public DocumentRequest create(DocumentRequest request) {
        request.setCreationDate(LocalDateTime.now());
        request.setStatus(DocumentRequestStatus.PENDING);
        return documentRequestRepository.save(request);
    }

    public DocumentRequest updateStatus(Integer id, DocumentRequestStatus newStatus) {
        DocumentRequest request = getById(id);
        request.setStatus(newStatus);
        if (newStatus == DocumentRequestStatus.APPROVED || newStatus == DocumentRequestStatus.REJECTED) {
            request.setProcessingDate(LocalDateTime.now());
        }
        return documentRequestRepository.save(request);
    }
 
    public void delete(Integer id) {
        documentRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DocumentRequest not found with id: " + id));
        documentRequestRepository.deleteById(id);
    }

}
