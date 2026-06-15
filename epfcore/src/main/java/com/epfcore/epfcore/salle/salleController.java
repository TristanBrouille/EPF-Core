package com.epfcore.epfcore.salle;

import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

import com.epfcore.epfcore.campus.entity.Campus;
import com.epfcore.epfcore.campus.repository.CampusRepository;

@RestController
@RequestMapping("/salle")
@CrossOrigin(origins = "http://localhost:4200") // Permet à Angular de parler au Back
public class salleController {

    private final salleRepository database;
    private final CampusRepository campusRepository;

    // Injection du repository
    public salleController(salleRepository database, CampusRepository campusRepository) {
        this.database = database;
        this.campusRepository = campusRepository;
    }

    // 1. Sauvegarder la salle en BDD quand on clique sur ajouter en Front
    @PostMapping
    public salle createSalle(@RequestBody salle nouvelleSalle) {
        return database.save(nouvelleSalle);
    }

    // 2. Envoyer la liste des campus au Front
    @GetMapping("/enums/campus")
    public List<Campus> getCampusOptions() {
        return campusRepository.findAll(); // Va lire dynamiquement la table 'campus'
    }

    // 3. Envoyer la liste des types de salle au Front
    @GetMapping("/enums/types")
    public List<TypeSalle> getTypeOptions() {
        return Arrays.asList(TypeSalle.values());
    }
}
