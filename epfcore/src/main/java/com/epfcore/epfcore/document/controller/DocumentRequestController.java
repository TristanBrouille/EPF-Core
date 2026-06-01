package com.epfcore.epfcore.document.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.epfcore.epfcore.document.entity.DocumentRequest;
import com.epfcore.epfcore.document.entity.DocumentRequest.DocumentRequestStatus;
import com.epfcore.epfcore.document.service.DocumentRequestService;
import com.epfcore.epfcore.document.service.GenerationCertificateService;

@RestController
@RequestMapping("/api/document-requests")
public class DocumentRequestController {

    private final DocumentRequestService documentRequestService;
    private final GenerationCertificateService generationCertificateService;

    public DocumentRequestController(DocumentRequestService documentRequestService,
            GenerationCertificateService generationCertificateService) {
        this.documentRequestService = documentRequestService;
        this.generationCertificateService = generationCertificateService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentRequest>> getAll() {
        return ResponseEntity.ok(documentRequestService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentRequest> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(documentRequestService.getById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<DocumentRequest>> getByStudentId(@PathVariable Integer studentId) {
        return ResponseEntity.ok(documentRequestService.getByStudentId(studentId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DocumentRequest>> getByStatus(@PathVariable DocumentRequestStatus status) {
        return ResponseEntity.ok(documentRequestService.getByStatus(status));
    }

    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<DocumentRequest>> getByStudentIdAndStatus(
            @PathVariable Integer studentId,
            @PathVariable DocumentRequestStatus status) {
        return ResponseEntity.ok(documentRequestService.getByStudentIdAndStatus(studentId, status));
    }

    @PostMapping
    public ResponseEntity<DocumentRequest> create(@RequestBody DocumentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentRequestService.create(request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DocumentRequest> updateStatus(
            @PathVariable Integer id,
            @RequestParam DocumentRequestStatus status) {
        return ResponseEntity.ok(documentRequestService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        documentRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/generate/certificate/{studentId}")
    public ResponseEntity<byte[]> generateCertificate(@PathVariable Integer studentId) {
        byte[] pdf = generationCertificateService.generateFromRequest(studentId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=certificat_scolarite.pdf")
                .body(pdf);
    }
}
