package com.epfcore.epfcore.document.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.epfcore.epfcore.document.entity.Document;
import com.epfcore.epfcore.document.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
 
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
 
    @GetMapping
    public ResponseEntity<List<Document>> getAll() {
        return ResponseEntity.ok(documentService.getAll());
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<Document> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(documentService.getById(id));
    }
 
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Document>> getByStudentId(@PathVariable Integer studentId) {
        return ResponseEntity.ok(documentService.getByStudentId(studentId));
    }
 
    @GetMapping("/request/{requestId}")
    public ResponseEntity<Document> getByRequestId(@PathVariable Integer requestId) {
        return ResponseEntity.ok(documentService.getByRequestId(requestId));
    }
 
    @GetMapping("/student/{studentId}/year/{academicYear}")
    public ResponseEntity<List<Document>> getByStudentIdAndAcademicYear(
            @PathVariable Integer studentId,
            @PathVariable String academicYear) {
        return ResponseEntity.ok(documentService.getByStudentIdAndAcademicYear(studentId, academicYear));
    }
 
    @PostMapping
    public ResponseEntity<Document> create(@RequestBody Document document) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.create(document));
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<Document> update(@PathVariable Integer id, @RequestBody Document document) {
        return ResponseEntity.ok(documentService.update(id, document));
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    } 
}
