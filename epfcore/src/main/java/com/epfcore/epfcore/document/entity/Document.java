package com.epfcore.epfcore.document.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.epfcore.epfcore.student.entity.Student;


@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
 
    @ManyToOne
    @JoinColumn(name = "request_id")
    private DocumentRequest request;
 
    @Column(name = "document_type", nullable = false, length = 11)
    @Enumerated(EnumType.STRING)
    private DocumentRequest.DocumentType documentType;
 
    @Column(name = "academic_year", length = 9)
    private String academicYear;
 
    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();
 
    @Column(name = "file_url", length = 255)
    private String fileUrl;
 
    @Column(name = "photo_url", columnDefinition = "TEXT")
    private String photoUrl;
 
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
 
    public Document() {}
 
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
 
    public DocumentRequest getRequest() {
        return request;
    }
 
    public void setRequest(DocumentRequest request) {
        this.request = request;
    }
 
    public DocumentRequest.DocumentType getDocumentType() {
        return documentType;
    }
 
    public void setDocumentType(DocumentRequest.DocumentType documentType) {
        this.documentType = documentType;
    }
 
    public String getAcademicYear() {
        return academicYear;
    }
 
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
 
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
 
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
 
    public String getFileUrl() {
        return fileUrl;
    }
 
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
 
    public String getPhotoUrl() {
        return photoUrl;
    }
 
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    
}
