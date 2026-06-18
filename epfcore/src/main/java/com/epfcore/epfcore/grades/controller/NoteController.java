package com.epfcore.epfcore.grades.controller;
 
import com.epfcore.epfcore.grades.entities.*;
import com.epfcore.epfcore.grades.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api")

public class NoteController {
 
    @Autowired
    private NoteService noteService;
 
    @PostMapping("/carnets")
    public ResponseEntity<?> creerCarnet(@RequestBody Map<String, Object> body) {
        try {
            String intitule        = (String) body.get("intitule");
            String anneeAcademique = (String) body.get("anneeAcademique");
            Long ueId     = body.get("uniteEnseignementId") != null
                            ? Long.valueOf(body.get("uniteEnseignementId").toString()) : null;
            Long moduleId = body.get("moduleId") != null
                            ? Long.valueOf(body.get("moduleId").toString()) : null;
 
            CarnetDeNotes carnet = noteService.creerCarnet(intitule, anneeAcademique, ueId, moduleId);
            return ResponseEntity.ok(toCarnetMap(carnet));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
 
    @GetMapping("/carnets")
    public ResponseEntity<?> getAllCarnets() {
        List<CarnetDeNotes> carnets = noteService.findAllCarnets();
        List<Map<String, Object>> result = new ArrayList<>();
        for (CarnetDeNotes c : carnets) result.add(toCarnetMap(c));
        return ResponseEntity.ok(result);
    }
 
    @GetMapping("/carnets/{id}")
    public ResponseEntity<?> getCarnet(@PathVariable long id) {
        CarnetDeNotes c = noteService.findCarnetById(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toCarnetMap(c));
    }
 
    @PatchMapping("/carnets/{id}/publier")
    public ResponseEntity<?> publierCarnet(@PathVariable long id) {
        try {
            CarnetDeNotes c = noteService.publierCarnet(id);
            return ResponseEntity.ok(toCarnetMap(c));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/carnets/{carnetId}/evaluations")
    public ResponseEntity<?> ajouterEvaluation(@PathVariable long carnetId,
                                               @RequestBody Map<String, Object> body) {
        try {
            String intitule = (String) body.get("intitule");
            String type     = body.getOrDefault("type", "DS").toString();
            float coef      = Float.parseFloat(body.getOrDefault("coef", "1").toString());
            float noteMax   = Float.parseFloat(body.getOrDefault("noteMax", "20").toString());
 
            Evaluation eval = noteService.ajouterEvaluation(carnetId, intitule, type, coef, noteMax);
            return ResponseEntity.ok(toEvalMap(eval));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
 
    @GetMapping("/carnets/{carnetId}/evaluations")
    public ResponseEntity<?> getEvaluations(@PathVariable long carnetId) {
        List<Evaluation> evals = noteService.findEvaluationsByCarnet(carnetId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Evaluation e : evals) result.add(toEvalMap(e));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/notes/saisir")
    public ResponseEntity<?> saisirNote(@RequestBody Map<String, Object> body) {
        try {
            long  evaluationId = Long.parseLong(body.get("evaluationId").toString());
            long  etudiantId   = Long.parseLong(body.get("etudiantId").toString());
            Float valeur       = body.get("valeur") != null
                                 ? Float.parseFloat(body.get("valeur").toString()) : null;
            String commentaire = (String) body.get("commentaire");
 
            Note note = noteService.saisirNote(evaluationId, etudiantId, valeur, commentaire);
            return ResponseEntity.ok(toNoteMap(note));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/notes/import/{evaluationId}")
    public ResponseEntity<?> importCSV(@PathVariable long evaluationId,
                                       @RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = noteService.importerNotesCSV(evaluationId, file);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
 
    @GetMapping("/carnets/{carnetId}/moyennes")
    public ResponseEntity<?> getMoyennes(@PathVariable long carnetId) {
        try {
            List<Etudiant> etudiants = noteService.findEtudiantsByCarnet(carnetId);
            List<Map<String, Object>> result = new ArrayList<>();

            for (Etudiant e : etudiants) {
                Float moy = noteService.calculerMoyenneEtudiant(carnetId, e.getId());

                Map<String, Object> row = new LinkedHashMap<>();
                row.put("etudiantId", e.getId());
                row.put("studentNumber", e.getStudentNumber());
                row.put("userId", e.getUserId());
                row.put("campus", e.getCampus());
                row.put("moyenne", moy);

                result.add(row);
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
 
    private Map<String, Object> toCarnetMap(CarnetDeNotes c) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id",              c.getId());
        m.put("intitule",        c.getIntitule());
        m.put("anneeAcademique", c.getAnneeAcademique());
        m.put("statut",          c.getStatut());
        m.put("moyenneClasse",   c.getMoyenneClasse());
        m.put("dateCreation",    c.getDateCreation() != null ? c.getDateCreation().toString() : null);
        m.put("datePublication", c.getDatePublication() != null ? c.getDatePublication().toString() : null);
        if (c.getUniteEnseignement() != null) {
            m.put("uniteEnseignementId",      c.getUniteEnseignement().getId());
            m.put("uniteEnseignementIntitule", c.getUniteEnseignement().getIntitule());
        }
        //m.put("nbEvaluations", c.getEvaluations().size());
        Long nbEvals = (Long) noteService.countEvaluationsByCarnet(c.getId());
        m.put("nbEvaluations", nbEvals != null ? nbEvals : 0);
        return m;
    }
 
    private Map<String, Object> toEvalMap(Evaluation e) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id",       e.getId());
        m.put("intitule", e.getIntitule());
        m.put("type",     e.getType());
        m.put("coef",     e.getCoef());
        m.put("noteMax",  e.getNoteMax());
        m.put("dateEval", e.getDateEval() != null ? e.getDateEval().toString() : null);
        m.put("carnetId", e.getCarnet().getId());
        return m;
    }
 
    private Map<String, Object> toNoteMap(Note n) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id",          n.getId());
        m.put("valeurNote",  n.getValeurNote());
        m.put("absent",      n.isAbsent());
        m.put("commentaire", n.getCommentaire());
        m.put("source",      n.getSource());
        m.put("dateSaisie",  n.getDateSaisie() != null ? n.getDateSaisie().toString() : null);
        m.put("etudiantId",  n.getEtudiant().getId());
        m.put("evaluationId",n.getEvaluation().getId());
        return m;
    }
}