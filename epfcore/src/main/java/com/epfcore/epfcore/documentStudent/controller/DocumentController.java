package com.epfcore.epfcore.documentStudent.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.stream.Collectors;

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

import com.epfcore.epfcore.documentStudent.dto.DocumentStudentDto;
import com.epfcore.epfcore.documentStudent.entity.DocumentStudent;
import com.epfcore.epfcore.documentStudent.service.DocumentStudentService;
import com.epfcore.epfcore.documentStudent.service.GenerationCertificateService;
import com.epfcore.epfcore.documentStudent.service.GenerationPdfService;

@RestController
@RequestMapping("/api/document_student")
public class DocumentController {

    private final DocumentStudentService documentService;
    private final GenerationCertificateService generationCertificateService;
    private final GenerationPdfService generationPdfService;

    public DocumentController(DocumentStudentService documentService,
            GenerationCertificateService generationCertificateService,
            GenerationPdfService generationPdfService) {
        this.documentService = documentService;
        this.generationCertificateService = generationCertificateService;
        this.generationPdfService = generationPdfService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentStudent>> getAll() {
        return ResponseEntity.ok(documentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentStudent> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(documentService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DocumentStudent>> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(documentService.getByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<DocumentStudent> create(@RequestBody DocumentStudent document) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.create(document));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentStudent> update(@PathVariable Integer id, @RequestBody DocumentStudent document) {
        return ResponseEntity.ok(documentService.update(id, document));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/generate/certificate/{studentId}")
    public ResponseEntity<byte[]> generateCertificate(@PathVariable Integer studentId) {
        byte[] pdf = generationCertificateService.generateFromRequest(studentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "certificat_scolarite.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping("/generate/pdf/{studentId}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Integer studentId) {
        byte[] pdf = generationPdfService.generateFromRequest(studentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "infos_personnelles.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<List<DocumentStudentDto>> getByUserIdSummary(@PathVariable Integer userId) {
        List<DocumentStudentDto> summaries = documentService.getByUserId(userId)
                .stream()
                .map(DocumentStudentDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }
}
