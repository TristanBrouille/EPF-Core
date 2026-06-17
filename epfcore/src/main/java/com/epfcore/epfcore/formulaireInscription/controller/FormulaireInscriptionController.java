package com.epfcore.epfcore.formulaireInscription.controller;

import com.epfcore.epfcore.formulaireInscription.dto.FormulaireInscriptionDTO;
import com.epfcore.epfcore.formulaireInscription.entity.DecisionAdmission;
import com.epfcore.epfcore.formulaireInscription.service.FormulaireInscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formulaire")
public class FormulaireInscriptionController {

    private final FormulaireInscriptionService formulaireService;

    public FormulaireInscriptionController(FormulaireInscriptionService formulaireService) {
        this.formulaireService = formulaireService;
    }

    @PostMapping
    public ResponseEntity<FormulaireInscriptionDTO> saveFormulaire(
            @RequestBody FormulaireInscriptionDTO formulaire,
            Authentication authentication
    ) {
        return ResponseEntity.ok(formulaireService.save(formulaire, authentication.getName()));
    }

    @PutMapping
    public ResponseEntity<FormulaireInscriptionDTO> updateFormulaire(
            @RequestBody FormulaireInscriptionDTO formulaire,
            Authentication authentication
    ) {
        return ResponseEntity.ok(formulaireService.update(formulaire, authentication.getName()));
    }

    @GetMapping("/candidatform")
    public ResponseEntity<FormulaireInscriptionDTO> getMyFormulaire(Authentication authentication) {
        return ResponseEntity.ok(formulaireService.getByEmail(authentication.getName()));
    }

    @PreAuthorize("hasAuthority('GESTIONNAIRE_ADMISSION')")
    @GetMapping("/{id}")
    public ResponseEntity<FormulaireInscriptionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(formulaireService.getById(id));
    }

    @PreAuthorize("hasAuthority('GESTIONNAIRE_ADMISSION')")
    @GetMapping("/soumis")
    public ResponseEntity<List<FormulaireInscriptionDTO>> getAllSoumis() {
        return ResponseEntity.ok(formulaireService.getAllSoumis());
    }

    @PreAuthorize("hasAuthority('GESTIONNAIRE_ADMISSION')")
    @GetMapping
    public ResponseEntity<List<FormulaireInscriptionDTO>> getAll() {
        return ResponseEntity.ok(formulaireService.getAll());
    }

    @PreAuthorize("hasAuthority('GESTIONNAIRE_ADMISSION')")
    @PatchMapping("/{id}/decision")
    public ResponseEntity<FormulaireInscriptionDTO> updateDecision(
            @PathVariable Long id,
            @RequestParam DecisionAdmission decision
    ) {
        return ResponseEntity.ok(formulaireService.updateDecision(id, decision));
    }

    @PreAuthorize("hasAuthority('GESTIONNAIRE_ADMISSION')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        formulaireService.delete(id, authentication);
        return ResponseEntity.noContent().build();
    }
}