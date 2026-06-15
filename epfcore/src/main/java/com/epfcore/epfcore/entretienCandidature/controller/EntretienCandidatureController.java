package com.epfcore.epfcore.entretienCandidature.controller;

import com.epfcore.epfcore.entretienCandidature.dto.EntretienCandidatureDTO;
import com.epfcore.epfcore.entretienCandidature.dto.InterviewerDTO;
import com.epfcore.epfcore.entretienCandidature.service.EntretienCandidatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entretien")
public class EntretienCandidatureController {

    private final EntretienCandidatureService entretienService;

    public EntretienCandidatureController(EntretienCandidatureService entretienService) {
        this.entretienService = entretienService;
    }

    @PostMapping
    public ResponseEntity<EntretienCandidatureDTO> create(@RequestBody EntretienCandidatureDTO entretien) {
        return ResponseEntity.ok(entretienService.create(entretien));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntretienCandidatureDTO> update(@PathVariable Long id, @RequestBody EntretienCandidatureDTO entretien) {
        return ResponseEntity.ok(entretienService.update(id, entretien));
    }

    @GetMapping("/mes-entretiens")
    public ResponseEntity<List<EntretienCandidatureDTO>> getMyEntretiens(Authentication authentication) {
        return ResponseEntity.ok(entretienService.getMyEntretiens(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntretienCandidatureDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(entretienService.getById(id));
    }

    @GetMapping("/interviewers")
    public ResponseEntity<List<InterviewerDTO>> getInterviewers() {
        return ResponseEntity.ok(entretienService.getInterviewers());
    }

    @GetMapping("/formulaire/{formulaireId}")
    public ResponseEntity<List<EntretienCandidatureDTO>> getByFormulaireId(@PathVariable Long formulaireId) {
        return ResponseEntity.ok(entretienService.getByFormulaireId(formulaireId));
    }

    @GetMapping
    public ResponseEntity<List<EntretienCandidatureDTO>> getAll() {
        return ResponseEntity.ok(entretienService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        entretienService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
