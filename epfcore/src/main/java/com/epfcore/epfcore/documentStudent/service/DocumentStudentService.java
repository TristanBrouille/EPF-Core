package com.epfcore.epfcore.documentStudent.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.epfcore.epfcore.documentStudent.entity.DocumentStudent;
import com.epfcore.epfcore.documentStudent.repository.DocumentStudentRepository;

@Service
public class DocumentStudentService {

    private final DocumentStudentRepository documentRepository;
 
    public DocumentStudentService(DocumentStudentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }
 
    public List<DocumentStudent> getAll() {
        return documentRepository.findAll();
    }
 
    public DocumentStudent getById(Integer id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }
 
    public List<DocumentStudent> getByUserId(Integer userId) {
        return documentRepository.findByUserId(userId);
    }

    public List<DocumentStudent> getByStatus(DocumentStudent status) {
        return documentRepository.findByStatus(status);
    }

    public List<DocumentStudent> getByUserIdAndStatus(Integer userId, DocumentStudent status) {
        return documentRepository.findByUserIdAndStatus(userId, status);
    }
 
    public DocumentStudent create(DocumentStudent document) {
        document.setCreationDate(LocalDateTime.now());
        return documentRepository.save(document);
    }
 
    public DocumentStudent update(Integer id, DocumentStudent updated) {
        DocumentStudent existing = getById(id);
        existing.setDocumentType(updated.getDocumentType());
        return documentRepository.save(existing);
    }

    public void delete(Integer id) {
        documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        documentRepository.deleteById(id);
    }
}
