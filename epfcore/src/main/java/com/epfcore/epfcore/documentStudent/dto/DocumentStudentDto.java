package com.epfcore.epfcore.documentStudent.dto;

import com.epfcore.epfcore.documentStudent.entity.DocumentStudent;
import java.time.LocalDateTime;

public class DocumentStudentDto {

    private Integer id;
    private Integer userId;
    private String documentType;
    private String status;
    private LocalDateTime creationDate;
    private LocalDateTime processingDate;
    private boolean hasFile;

    public DocumentStudentDto(DocumentStudent doc) {
        this.id = doc.getId();
        this.userId = doc.getUserId();
        this.documentType = doc.getDocumentType().toString();
        this.status = doc.getStatus().toString();
        this.creationDate = doc.getCreationDate();
        this.processingDate = doc.getProcessingDate();
        this.hasFile = doc.getFileUrl() != null && !doc.getFileUrl().isEmpty();
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getProcessingDate() {
        return processingDate;
    }

    public boolean isHasFile() {
        return hasFile;
    }

}
