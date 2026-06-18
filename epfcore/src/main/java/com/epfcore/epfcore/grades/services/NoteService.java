package com.epfcore.epfcore.grades.services;

import com.epfcore.epfcore.grades.entities.*;
import com.epfcore.epfcore.grades.entities.Module;
import com.epfcore.epfcore.grades.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.epfcore.epfcore.student.repository.StudentRepository;
import com.epfcore.epfcore.student.entity.Student;
import com.epfcore.epfcore.grades.dto.*;

@Service
@Transactional
public class NoteService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private StudentRepository studentRepository;

    //  CARNET DE NOTES

    public CarnetDeNotes creerCarnet(String intitule, String anneeAcademique,
                                     Long ueId, Long moduleId) {
        CarnetDeNotes carnet = new CarnetDeNotes();
        carnet.setIntitule(intitule);
        carnet.setAnneeAcademique(anneeAcademique);
        carnet.setStatut("BROUILLON");

        if (ueId != null) {
            UniteEnseignement ue = em.find(UniteEnseignement.class, ueId);
            carnet.setUniteEnseignement(ue);
        }
        if (moduleId != null) {
            Module mod = em.find(Module.class, moduleId);
            carnet.setModule(mod);
        }

        em.persist(carnet);
        return carnet;
    }

    @Transactional(readOnly = true)
    public List<CarnetDeNotes> findAllCarnets() {
        return em.createQuery(
            "SELECT DISTINCT c FROM CarnetDeNotes c " +
            "LEFT JOIN FETCH c.uniteEnseignement " +
            "ORDER BY c.anneeAcademique DESC, c.intitule", CarnetDeNotes.class)
            .getResultList();
    }

    @Transactional(readOnly = true)
    public long countEvaluationsByCarnet(long carnetId) {
        Long count = em.createQuery(
            "SELECT COUNT(e) FROM Evaluation e WHERE e.carnet.id = :id",
            Long.class)
            .setParameter("id", carnetId)
            .getSingleResult();
        return count != null ? count : 0L;
    }

    @Transactional(readOnly = true)
    public CarnetDeNotes findCarnetById(long id) {
        return em.find(CarnetDeNotes.class, id);
    }

    // Publie un carnet (BROUILLON → PUBLIE).

    public CarnetDeNotes publierCarnet(long carnetId) {
        CarnetDeNotes carnet = em.find(CarnetDeNotes.class, carnetId);
        if (carnet == null) throw new IllegalArgumentException("Carnet introuvable : " + carnetId);
        if (carnet.isPublie()) throw new IllegalStateException("Le carnet est déjà publié.");

        carnet.setStatut("PUBLIE");
        carnet.setDatePublication(LocalDateTime.now());
        return carnet;
    }

    //  ÉVALUATIONS

    public Evaluation ajouterEvaluation(long carnetId, String intitule,
                                        String type, float coef, float noteMax) {
        CarnetDeNotes carnet = em.find(CarnetDeNotes.class, carnetId);
        if (carnet == null) throw new IllegalArgumentException("Carnet introuvable");
        if (carnet.isPublie()) throw new IllegalStateException("Carnet publié, modification interdite.");

        Evaluation eval = new Evaluation(intitule, type, coef, noteMax, carnet);
        em.persist(eval);
        return eval;
    }

    @Transactional(readOnly = true)
    public List<Evaluation> findEvaluationsByCarnet(long carnetId) {
        return em.createQuery(
            "SELECT e FROM Evaluation e WHERE e.carnet.id = :id ORDER BY e.dateEval",
            Evaluation.class)
            .setParameter("id", carnetId)
            .getResultList();
    }

    //  SAISIE MANUELLE DES NOTES

    public Note saisirNote(long evaluationId, long etudiantId,
                        Float valeur, String commentaire) {
        Evaluation eval = em.find(Evaluation.class, evaluationId);
        if (eval == null) throw new IllegalArgumentException("Évaluation introuvable");
        if (eval.getCarnet().isPublie())
            throw new IllegalStateException("Carnet publié : modification interdite.");

        studentRepository.findById(etudiantId)
            .orElseThrow(() -> new IllegalArgumentException("Étudiant introuvable : " + etudiantId));

        List<Note> existing = em.createQuery(
            "SELECT n FROM Note n WHERE n.evaluation.id=:eid AND n.etudiantId=:sid",
            Note.class)
            .setParameter("eid", evaluationId)
            .setParameter("sid", etudiantId)
            .getResultList();

        Note note;
        if (!existing.isEmpty()) {
            note = existing.get(0);
            note.setValeurNote(valeur);
            note.setCommentaire(commentaire);
            note.setSource("MANUELLE");
            note.setDateSaisie(LocalDateTime.now());
        } else {
            note = new Note();
            note.setEtudiantId(etudiantId);
            note.setEvaluation(eval);
            note.setValeurNote(valeur);
            note.setCommentaire(commentaire);
            note.setSource("MANUELLE");
            em.persist(note);
        }
        return note;
    }

    public Map<String, Object> importerNotesCSV(long evaluationId, MultipartFile file) {
        Evaluation eval = em.find(Evaluation.class, evaluationId);
        if (eval == null) throw new IllegalArgumentException("Évaluation introuvable");
        if (eval.getCarnet().isPublie())
            throw new IllegalStateException("Carnet publié : import interdit.");

        int success = 0, errors = 0;
        List<String> errorLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("etudiant")) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 2) {
                    errorLines.add("Ligne " + lineNum + " : format invalide → " + line);
                    errors++;
                    continue;
                }

                String numero  = parts[0].trim();
                String valStr  = parts[1].trim();
                String comment = parts.length > 2 ? parts[2].trim() : null;

                Optional<Student> studentOpt = studentRepository.findByStudentNumber(numero);
                if (studentOpt.isEmpty()) {
                    errorLines.add("Ligne " + lineNum + " : étudiant introuvable → " + numero);
                    errors++;
                    continue;
                }
                Student student = studentOpt.get();
                Long etudiantId = student.getId();

                Float valeur = null;
                boolean absent = false;

                if ("ABS".equalsIgnoreCase(valStr) || "ABSENT".equalsIgnoreCase(valStr)) {
                    absent = true;
                } else {
                    try {
                        valeur = Float.parseFloat(valStr.replace(',', '.'));
                        if (valeur < 0 || valeur > eval.getNoteMax()) {
                            errorLines.add("Ligne " + lineNum + " : note hors barème → " + valStr);
                            errors++;
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        errorLines.add("Ligne " + lineNum + " : valeur invalide → " + valStr);
                        errors++;
                        continue;
                    }
                }

                // Upsert
                try {
                    saisirNoteInterne(eval, etudiantId, valeur, comment, absent, "IMPORT_CSV");
                    success++;
                } catch (Exception e) {
                    errorLines.add("Ligne " + lineNum + " : erreur sauvegarde → " + e.getMessage());
                    errors++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lecture CSV : " + e.getMessage(), e);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("imported", success);
        result.put("errors",   errors);
        result.put("errorDetails", errorLines);
        return result;
    }

    private void saisirNoteInterne(Evaluation eval, Long etudiantId,
                                Float valeur, String commentaire,
                                boolean absent, String source) {
        List<Note> existing = em.createQuery(
            "SELECT n FROM Note n WHERE n.evaluation.id=:eid AND n.etudiantId=:sid",
            Note.class)
            .setParameter("eid", eval.getId())
            .setParameter("sid", etudiantId)
            .getResultList();

        if (!existing.isEmpty()) {
            Note n = existing.get(0);
            n.setValeurNote(valeur);
            n.setAbsent(absent);
            n.setCommentaire(commentaire);
            n.setSource(source);
            n.setDateSaisie(LocalDateTime.now());
        } else {
            Note n = new Note(valeur, etudiantId, eval, source);
            n.setAbsent(absent);
            n.setCommentaire(commentaire);
            em.persist(n);
        }
    }

    //  CALCUL MOYENNE ÉTUDIANT POUR UN CARNET

    @Transactional(readOnly = true)
    public Float calculerMoyenneEtudiant(long carnetId, long etudiantId) {
        List<Object[]> rows = em.createQuery(
            "SELECT n.valeurNote, e.coef FROM Note n " +
            "JOIN n.evaluation e " +
            "WHERE e.carnet.id=:cid AND n.etudiantId=:eid AND n.absent=false",
            Object[].class)
            .setParameter("cid", carnetId)
            .setParameter("eid", etudiantId)
            .getResultList();

        if (rows.isEmpty()) return null;

        double sumPonderated = 0, sumCoef = 0;
        for (Object[] row : rows) {
            Float note = (Float) row[0];
            Float coef = (Float) row[1];
            if (note != null && coef != null) {
                sumPonderated += note * coef;
                sumCoef       += coef;
            }
        }
        return sumCoef == 0 ? null : (float)(sumPonderated / sumCoef);
    }

    @Transactional(readOnly = true)
    public List<Etudiant> findEtudiantsByCarnet(long carnetId) {
        return em.createQuery(
            "SELECT DISTINCT n.etudiant FROM Note n " +
            "JOIN n.evaluation e WHERE e.carnet.id=:cid",
            Etudiant.class)
            .setParameter("cid", carnetId)
            .getResultList();
    }

    @Transactional(readOnly = true)
    public List<MoyenneRowDto> getMoyennesParCarnet(long carnetId) {
        List<Long> ids = em.createQuery(
            "SELECT DISTINCT n.etudiantId FROM Note n JOIN n.evaluation e WHERE e.carnet.id=:cid",
            Long.class)
            .setParameter("cid", carnetId)
            .getResultList();

        List<MoyenneRowDto> result = new ArrayList<>();
        for (Long etudiantId : ids) {
            studentRepository.findById(etudiantId).ifPresent(student -> {
                Float moy = calculerMoyenneEtudiant(carnetId, etudiantId);
                String nomComplet = student.getUser().getFirstname() + " " + student.getUser().getLastname();
                result.add(new MoyenneRowDto(etudiantId, student.getStudentNumber(), nomComplet, moy));
            });
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<MoyenneRowDto> getEtudiantsPourCarnet(long carnetId) {
        CarnetDeNotes carnet = em.find(CarnetDeNotes.class, carnetId);
        if (carnet == null) throw new IllegalArgumentException("Carnet introuvable");

        return studentRepository.findAll().stream().map(s -> {
            Float moy = calculerMoyenneEtudiant(carnetId, s.getId());
            String nom = s.getUser().getFirstname() + " " + s.getUser().getLastname();
            return new MoyenneRowDto(s.getId(), s.getStudentNumber(), nom, moy);
        }).toList();
    }

    @Transactional(readOnly = true)
    public List<NoteDTO> getNotesByCarnet(long carnetId) {
        return em.createQuery(
            "SELECT n FROM Note n JOIN n.evaluation e WHERE e.carnet.id=:cid",
            Note.class)
            .setParameter("cid", carnetId)
            .getResultList()
            .stream().map(NoteDTO::new).toList();
    }
}