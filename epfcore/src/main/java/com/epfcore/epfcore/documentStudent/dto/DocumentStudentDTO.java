package com.epfcore.epfcore.documentStudent.dto;

import java.time.LocalDateTime;

import com.epfcore.epfcore.documentStudent.entity.DocumentStudent;
import com.epfcore.epfcore.documentStudent.entity.DocumentStatus;
import com.epfcore.epfcore.documentStudent.entity.DocumentType;

public class DocumentStudentDTO {


    private Integer id;
    private DocumentType documentType;
    private DocumentStatus status;
    private LocalDateTime creationDate;
    private LocalDateTime processingDate;
    private LocalDateTime archivedAt;
    private String archivedBy;

    private Integer userId;
    private Long studentId;
    private String firstname;
    private String lastname;

    public DocumentStudentDTO(Long studentId,DocumentStudent doc, String firstname, String lastname) {
        this.id = doc.getId();
        this.documentType = doc.getDocumentType();
        this.status = doc.getStatus();
        this.creationDate = doc.getCreationDate();
        this.processingDate = doc.getProcessingDate();
        this.archivedAt = doc.getArchivedAt();
        this.archivedBy = doc.getArchivedBy();
        this.userId = doc.getUserId();
        this.firstname = firstname;
        this.lastname = lastname;
        this.studentId = studentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public DocumentStudentDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    public String getArchivedBy() {
        return archivedBy;
    }

    public void setArchivedBy(String archivedBy) {
        this.archivedBy = archivedBy;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
