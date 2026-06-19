package com.epfcore.epfcore.security.exposition;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidats")
public class CandidatController {

    private final CandidatService candidatService;

    public CandidatController(CandidatService candidatService) {
        this.candidatService = candidatService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserWithCaptcha candidat) {
        return ResponseEntity.ok(candidatService.register(candidat));
    }
}