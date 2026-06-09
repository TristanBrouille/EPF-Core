package com.epfcore.epfcore.document.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.epfcore.epfcore.document.entity.Document;
import com.epfcore.epfcore.document.repository.DocumentRepository;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
 
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }
 
    public List<Document> getAll() {
        return documentRepository.findAll();
    }
 
    public Document getById(Integer id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }
 
    public List<Document> getByUserId(Integer userId) {
        return documentRepository.findByUserId(userId);
    }

    public List<Document> getByStatus(Document status) {
        return documentRepository.findByStatus(status);
    }

    public List<Document> getByUserIdAndStatus(Integer userId, Document status) {
        return documentRepository.findByUserIdAndStatus(userId, status);
    }
 
    public Document create(Document document) {
        document.setCreationDate(LocalDateTime.now());
        return documentRepository.save(document);
    }
 
    public Document update(Integer id, Document updated) {
        Document existing = getById(id);
        existing.setDocumentType(updated.getDocumentType());
        return documentRepository.save(existing);
    }

    public void delete(Integer id) {
        documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        documentRepository.deleteById(id);
    }
}
