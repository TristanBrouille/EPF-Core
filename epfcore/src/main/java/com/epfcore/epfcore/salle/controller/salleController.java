package com.epfcore.epfcore.salle.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

import com.epfcore.epfcore.campus.entity.Campus;
import com.epfcore.epfcore.campus.repository.CampusRepository;
import com.epfcore.epfcore.salle.entity.TypeSalle;
import com.epfcore.epfcore.salle.entity.salle;
import com.epfcore.epfcore.salle.repository.salleRepository;

@RestController
@RequestMapping("/salle")
public class salleController {

    private final salleRepository database;
    private final CampusRepository campusRepository;

    public salleController(salleRepository database, CampusRepository campusRepository) {
        this.database = database;
        this.campusRepository = campusRepository;
    }

    @PostMapping
    public salle createSalle(@RequestBody salle nouvelleSalle) {
        return database.save(nouvelleSalle);
    }

    @GetMapping("/enums/campus")
    public List<Campus> getCampusOptions() {
        return campusRepository.findAll(); 
    }

    @GetMapping("/enums/types")
    public List<TypeSalle> getTypeOptions() {
        return Arrays.asList(TypeSalle.values());
    }

    @GetMapping
    public ResponseEntity<List<salle>> getAllSalles() {
        return ResponseEntity.ok(database.findAll()); 
    }
}
