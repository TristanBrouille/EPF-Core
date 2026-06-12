package com.epfcore.epfcore.documentFormulaire.controller;

import com.epfcore.epfcore.documentFormulaire.dto.DocumentFormulaireDTO;
import com.epfcore.epfcore.documentFormulaire.service.DocumentFormulaireService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/formulaire/{formulaireId}/documents")
public class DocumentFormulaireController {

    private final DocumentFormulaireService documentService;

    public DocumentFormulaireController(DocumentFormulaireService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/{documentType}")
    public ResponseEntity<DocumentFormulaireDTO> uploadDocument(
            @PathVariable Long formulaireId,
            @PathVariable String documentType,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        return ResponseEntity.ok(documentService.store(formulaireId, documentType, file, authentication));
    }

    @GetMapping
    public ResponseEntity<List<DocumentFormulaireDTO>> getDocuments(
            @PathVariable Long formulaireId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(documentService.getByFormulaireId(formulaireId, authentication));
    }

    @GetMapping("/{documentType}")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long formulaireId,
            @PathVariable String documentType,
            Authentication authentication
    ) {
        Resource file = documentService.loadFile(formulaireId, documentType, authentication);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @DeleteMapping("/{documentType}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long formulaireId,
            @PathVariable String documentType,
            Authentication authentication
    ) {
        documentService.delete(formulaireId, documentType, authentication);
        return ResponseEntity.noContent().build();
    }
}
