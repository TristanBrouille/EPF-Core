package com.epfcore.epfcore.documentFormulaire.service;

import com.epfcore.epfcore.documentFormulaire.dto.DocumentFormulaireDTO;
import com.epfcore.epfcore.documentFormulaire.entity.DocumentFormulaire;
import com.epfcore.epfcore.documentFormulaire.repository.DocumentFormulaireRepository;
import com.epfcore.epfcore.documentFormulaire.storage.StorageService;
import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import com.epfcore.epfcore.formulaireInscription.repository.FormulaireInscriptionRepository;
import com.epfcore.epfcore.security.domain.Roles;
import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.infrastructure.UserJpaRepository;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentFormulaireService {

    private final DocumentFormulaireRepository documentRepository;
    private final FormulaireInscriptionRepository formulaireRepository;
    private final UserJpaRepository userRepository;
    private final StorageService storageService;

    public DocumentFormulaireService(
            DocumentFormulaireRepository documentRepository,
            FormulaireInscriptionRepository formulaireRepository,
            UserJpaRepository userRepository,
            StorageService storageService
    ) {
        this.documentRepository = documentRepository;
        this.formulaireRepository = formulaireRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public DocumentFormulaireDTO store(Long formulaireId, String documentType, MultipartFile file, Authentication authentication) {
        FormulaireInscription formulaire = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formulaire not found"));

        checkOwnership(formulaire, authentication);

        String extension = "";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        String storedFilename = formulaireId + "_" + documentType + extension;

        Optional<DocumentFormulaire> existing = documentRepository
                .findByFormulaireIdAndDocumentType(formulaireId, documentType);

        storageService.store(file, storedFilename);

        if (existing.isPresent() && !storedFilename.equals(existing.get().getFileUrl())) {
            storageService.delete(existing.get().getFileUrl());
        }

        DocumentFormulaire document = existing.orElseGet(() -> {
            DocumentFormulaire newDoc = new DocumentFormulaire();
            newDoc.setFormulaire(formulaire);
            newDoc.setDocumentType(documentType);
            return newDoc;
        });
        document.setFileUrl(storedFilename);
        document.setFileName(originalFilename);

        return toDTO(documentRepository.save(document));
    }

    public List<DocumentFormulaireDTO> getByFormulaireId(Long formulaireId, Authentication authentication) {
        FormulaireInscription formulaire = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formulaire not found"));

        checkOwnership(formulaire, authentication);

        return documentRepository.findByFormulaireId(formulaireId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Resource loadFile(Long formulaireId, String documentType, Authentication authentication) {
        FormulaireInscription formulaire = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formulaire not found"));

        checkOwnership(formulaire, authentication);

        DocumentFormulaire document = documentRepository
                .findByFormulaireIdAndDocumentType(formulaireId, documentType)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        return storageService.loadAsResource(document.getFileUrl());
    }

    public void delete(Long formulaireId, String documentType, Authentication authentication) {
        FormulaireInscription formulaire = formulaireRepository.findById(formulaireId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formulaire not found"));

        checkOwnership(formulaire, authentication);

        DocumentFormulaire document = documentRepository
                .findByFormulaireIdAndDocumentType(formulaireId, documentType)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        storageService.delete(document.getFileUrl());
        documentRepository.delete(document);
    }

    private void checkOwnership(FormulaireInscription formulaire, Authentication authentication) {
        User user = getUserByEmail(authentication.getName());
        if (user.getRole() == Roles.CANDIDAT && !formulaire.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

    private User getUserByEmail(String email) {
        User user = userRepository.ofEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    private DocumentFormulaireDTO toDTO(DocumentFormulaire document) {
        return new DocumentFormulaireDTO(
                document.getId(),
                document.getFormulaire().getId(),
                document.getDocumentType(),
                document.getFileUrl(),
                document.getFileName()
        );
    }
}
