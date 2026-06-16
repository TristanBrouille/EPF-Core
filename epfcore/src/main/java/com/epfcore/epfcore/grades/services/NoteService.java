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

@Service
@Transactional
public class NoteService {

    @PersistenceContext
    private EntityManager em;

    // ══════════════════════════════════════════════════════════════════════════
    //  CARNET DE NOTES
    // ══════════════════════════════════════════════════════════════════════════

    /** Crée un nouveau carnet de notes (statut BROUILLON). */
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

    /** Récupère tous les carnets. */
    @Transactional(readOnly = true)
    public List<CarnetDeNotes> findAllCarnets() {
        return em.createQuery(
            "SELECT c FROM CarnetDeNotes c " +
            "LEFT JOIN FETCH c.uniteEnseignement " +
            "LEFT JOIN FETCH c.evaluations " +
            "ORDER BY c.anneeAcademique DESC, c.intitule", CarnetDeNotes.class)
            .getResultList();
    }

    /** Récupère un carnet par id. */
    @Transactional(readOnly = true)
    public CarnetDeNotes findCarnetById(long id) {
        return em.find(CarnetDeNotes.class, id);
    }

    /**
     * Publie un carnet (BROUILLON → PUBLIE).
     * Une fois publié, les notes ne peuvent plus être modifiées directement.
     */
    public CarnetDeNotes publierCarnet(long carnetId) {
        CarnetDeNotes carnet = em.find(CarnetDeNotes.class, carnetId);
        if (carnet == null) throw new IllegalArgumentException("Carnet introuvable : " + carnetId);
        if (carnet.isPublie()) throw new IllegalStateException("Le carnet est déjà publié.");

        carnet.setStatut("PUBLIE");
        carnet.setDatePublication(LocalDateTime.now());
        return carnet;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ÉVALUATIONS
    // ══════════════════════════════════════════════════════════════════════════

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

    // ══════════════════════════════════════════════════════════════════════════
    //  SAISIE MANUELLE DES NOTES
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Sauvegarde ou met à jour une note pour un étudiant/évaluation.
     * Règle : si le carnet est PUBLIÉ, on refuse toute modification.
     */
    public Note saisirNote(long evaluationId, long etudiantId,
                           Float valeur, String commentaire) {
        Evaluation eval   = em.find(Evaluation.class, evaluationId);
        if (eval == null) throw new IllegalArgumentException("Évaluation introuvable");
        if (eval.getCarnet().isPublie())
            throw new IllegalStateException("Carnet publié : modification interdite.");

        Etudiant etudiant = em.find(Etudiant.class, etudiantId);
        if (etudiant == null) throw new IllegalArgumentException("Étudiant introuvable");

        // Recherche d'une note existante
        List<Note> existing = em.createQuery(
            "SELECT n FROM Note n WHERE n.evaluation.id=:eid AND n.etudiant.id=:sid",
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
            note = new Note(valeur, etudiant, eval, "MANUELLE");
            note.setCommentaire(commentaire);
            em.persist(note);
        }
        return note;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  IMPORT CSV
    //  Format attendu : etudiant_numero,valeur_note,commentaire
    //  Exemple :
    //    123456,14.5,
    //    123457,ABS,Absent justifié
    //    123458,12,
    // ══════════════════════════════════════════════════════════════════════════

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

                // Recherche étudiant par numéro
                List<Etudiant> etudiants = em.createQuery(
                    "SELECT e FROM Etudiant e WHERE e.numero = :num", Etudiant.class)
                    .setParameter("num", numero)
                    .getResultList();

                if (etudiants.isEmpty()) {
                    errorLines.add("Ligne " + lineNum + " : étudiant introuvable → " + numero);
                    errors++;
                    continue;
                }

                Etudiant etudiant = etudiants.get(0);
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
                    saisirNoteInterne(eval, etudiant, valeur, comment, absent, "IMPORT_CSV");
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

    private void saisirNoteInterne(Evaluation eval, Etudiant etudiant,
                                   Float valeur, String commentaire,
                                   boolean absent, String source) {
        List<Note> existing = em.createQuery(
            "SELECT n FROM Note n WHERE n.evaluation.id=:eid AND n.etudiant.id=:sid",
            Note.class)
            .setParameter("eid", eval.getId())
            .setParameter("sid", etudiant.getId())
            .getResultList();

        if (!existing.isEmpty()) {
            Note n = existing.get(0);
            n.setValeurNote(valeur);
            n.setAbsent(absent);
            n.setCommentaire(commentaire);
            n.setSource(source);
            n.setDateSaisie(LocalDateTime.now());
        } else {
            Note n = new Note(valeur, etudiant, eval, source);
            n.setAbsent(absent);
            n.setCommentaire(commentaire);
            em.persist(n);
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  CALCUL MOYENNE ÉTUDIANT POUR UN CARNET
    //  Moyenne pondérée : Σ(note_i × coef_i) / Σ(coef_i)
    // ══════════════════════════════════════════════════════════════════════════

    @Transactional(readOnly = true)
    public Float calculerMoyenneEtudiant(long carnetId, long etudiantId) {
        List<Object[]> rows = em.createQuery(
            "SELECT n.valeurNote, e.coef FROM Note n " +
            "JOIN n.evaluation e " +
            "WHERE e.carnet.id=:cid AND n.etudiant.id=:eid AND n.absent=false",
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

    /** Récupère tous les étudiants qui ont au moins une note dans ce carnet. */
    @Transactional(readOnly = true)
    public List<Etudiant> findEtudiantsByCarnet(long carnetId) {
        return em.createQuery(
            "SELECT DISTINCT n.etudiant FROM Note n " +
            "JOIN n.evaluation e WHERE e.carnet.id=:cid",
            Etudiant.class)
            .setParameter("cid", carnetId)
            .getResultList();
    }
}