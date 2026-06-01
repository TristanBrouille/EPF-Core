package com.epfcore.epfcore.document.entity;

import java.time.LocalDateTime;

import com.epfcore.epfcore.student.entity.Student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "document_request")
public class DocumentRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
 
    @Column(name = "document_type", nullable = false, length = 11)
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
 
    @Column(name = "status", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private DocumentRequestStatus status = DocumentRequestStatus.PENDING;
 
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();
 
    @Column(name = "processing_date")
    private LocalDateTime processingDate;
 
    // -------------------------------------------------------------------------
    // Enums
    // -------------------------------------------------------------------------
 
    public enum DocumentType {
        CERTIFICATE, INFOS
    }
 
    public enum DocumentRequestStatus {
        PENDING, IN_PROGRESS, APPROVED, REJECTED
    }
 
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
 
    public DocumentRequest() {}
 
    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------
 
    public Integer getId() {
        return id;
    }
 
    public Student getStudent() {
        return student;
    }
 
    public void setStudent(Student student) {
        this.student = student;
    }
 
    public DocumentType getDocumentType() {
        return documentType;
    }
 
    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
 
    public DocumentRequestStatus getStatus() {
        return status;
    }
 
    public void setStatus(DocumentRequestStatus status) {
        this.status = status;
    }
 
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
 
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
 
    public LocalDateTime getProcessingDate() {
        return processingDate;
    }
 
    public void setProcessingDate(LocalDateTime processingDate) {
        this.processingDate = processingDate;
    }


}
