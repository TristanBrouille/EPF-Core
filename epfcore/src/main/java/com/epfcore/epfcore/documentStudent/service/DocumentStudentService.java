package com.epfcore.epfcore.documentStudent.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.epfcore.epfcore.documentStudent.dto.DocumentStudentDTO;
import com.epfcore.epfcore.documentStudent.entity.DocumentStatus;
import com.epfcore.epfcore.documentStudent.entity.DocumentStudent;
import com.epfcore.epfcore.documentStudent.repository.DocumentStudentRepository;
import com.epfcore.epfcore.student.entity.Student;
import com.epfcore.epfcore.student.repository.StudentRepository;

@Service
public class DocumentStudentService {

    private final DocumentStudentRepository documentRepository;
    private final StudentRepository studentRepository;

    public DocumentStudentService(DocumentStudentRepository documentRepository, StudentRepository studentRepository) {
        this.documentRepository = documentRepository;
        this.studentRepository = studentRepository;
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

    public List<DocumentStudent> getByStatus(DocumentStatus status) {
        return documentRepository.findByStatus(status);
    }

    public List<DocumentStudent> getByUserIdAndStatus(Integer userId, DocumentStatus status) {
        return documentRepository.findByUserIdAndStatus(userId, status);
    }

    public List<DocumentStudentDTO> getArchivedByUserId(Integer userId) {
        return documentRepository.findByUserIdAndStatus(userId, DocumentStatus.ARCHIVED)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private DocumentStudentDTO toDTO(DocumentStudent doc) {
        Student student = studentRepository.findByUserId(doc.getUserId().longValue())
                .orElse(null);

        Long studentId = (student != null) ? student.getId() : null;
        String firstname = (student != null && student.getUser() != null)
                ? student.getUser().getFirstname()
                : null;
        String lastname = (student != null && student.getUser() != null)
                ? student.getUser().getLastname()
                : null;

        return new DocumentStudentDTO(studentId, doc,  firstname, lastname);
    }

    public DocumentStudent create(DocumentStudent document) {
        document.setCreationDate(LocalDateTime.now());
        return documentRepository.save(document);
    }

    public DocumentStudent update(Integer id, DocumentStudent updated) {
        DocumentStudent existing = getById(id);
        // Garde : un document archivé ne peut pas être modifié
        if (existing.getStatus() == DocumentStatus.ARCHIVED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Ce document est archivé et ne peut plus être modifié.");
        }
        existing.setDocumentType(updated.getDocumentType());
        return documentRepository.save(existing);
    }

    public void delete(Integer id) {
        DocumentStudent existing = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        // Garde : un document archivé ne peut pas être supprimé
        if (existing.getStatus() == DocumentStatus.ARCHIVED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Ce document est archivé et ne peut pas être supprimé.");
        }
        documentRepository.deleteById(id);
    }

    public DocumentStudent archive(Integer id, String archivedBy) {
        DocumentStudent document = getById(id);
        if (document.getStatus() == DocumentStatus.ARCHIVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ce document est déjà archivé.");
        }
        document.setStatus(DocumentStatus.ARCHIVED);
        document.setArchivedAt(LocalDateTime.now());
        document.setArchivedBy(archivedBy);
        return documentRepository.save(document);
    }

    public DocumentStudent unarchive(Integer id) {
        DocumentStudent doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document introuvable"));
        doc.setStatus(DocumentStatus.APPROVED);
        doc.setArchivedAt(null);
        doc.setArchivedBy(null);
        return documentRepository.save(doc);
    }

    public List<DocumentStudentDTO> getAllArchived() {
        return documentRepository.findByStatus(DocumentStatus.ARCHIVED)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
