package com.epfcore.epfcore.document.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document")
public class Document {

    public enum DocumentType {
        CERTIFICATE, INFOS
    }

    public enum DocumentStatus {
        PENDING, IN_PROGRESS, APPROVED, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "document_type", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "status", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    @Column(name = "file_url", columnDefinition = "TEXT")
    private String fileUrl;

    public Document() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    
}
