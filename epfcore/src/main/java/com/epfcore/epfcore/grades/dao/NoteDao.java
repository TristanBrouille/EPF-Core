package com.epfcore.epfcore.grades.dao;

import com.epfcore.epfcore.grades.entities.Note;
import com.epfcore.epfcore.grades.entities.Etudiant;
import com.epfcore.epfcore.grades.entities.Evaluation;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.epfcore.epfcore.grades.exception.DaoException;


public class NoteDao {

    private static NoteDao instance = null;
    private NoteDao() {}

    public static NoteDao getInstance() {
        if (instance == null) instance = new NoteDao();
        return instance;
    }

    // ── Requêtes SQL ──────────────────────────────────────────────────────────
    private static final String INSERT_NOTE =
        "INSERT INTO Note(valeur_note, absent, commentaire, source, date_saisie, etudiant_id, evaluation_id) " +
        "VALUES(?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE_NOTE =
        "UPDATE Note SET valeur_note=?, absent=?, commentaire=?, source=?, date_saisie=? " +
        "WHERE etudiant_id=? AND evaluation_id=?;";

    private static final String UPSERT_NOTE =
        "INSERT INTO Note(valeur_note, absent, commentaire, source, date_saisie, etudiant_id, evaluation_id) " +
        "VALUES(?, ?, ?, ?, ?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE valeur_note=VALUES(valeur_note), absent=VALUES(absent), " +
        "commentaire=VALUES(commentaire), source=VALUES(source), date_saisie=VALUES(date_saisie);";

    private static final String FIND_BY_EVALUATION =
        "SELECT n.id, n.valeur_note, n.absent, n.commentaire, n.source, n.date_saisie, " +
        "       n.etudiant_id, n.evaluation_id " +
        "FROM Note n WHERE n.evaluation_id = ?;";

    private static final String FIND_BY_ETUDIANT_AND_CARNET =
        "SELECT n.id, n.valeur_note, n.absent, n.commentaire, n.source, n.date_saisie, " +
        "       n.etudiant_id, n.evaluation_id " +
        "FROM Note n " +
        "JOIN Evaluation e ON e.id = n.evaluation_id " +
        "WHERE n.etudiant_id = ? AND e.carnet_id = ?;";

    private static final String DELETE_BY_EVALUATION =
        "DELETE FROM Note WHERE evaluation_id = ?;";

    private static final String AVG_BY_EVALUATION =
        "SELECT AVG(valeur_note) FROM Note WHERE evaluation_id = ? AND absent = FALSE;";

    // ── Opérations ────────────────────────────────────────────────────────────

    /**
     * Insère ou met à jour une note (upsert).
     * Utilisé aussi bien pour la saisie manuelle que pour l'import CSV.
     */
    public void saveOrUpdate(Note note) throws DaoException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPSERT_NOTE)) {

            ps.setObject(1, note.getValeurNote());    // peut être NULL (absent)
            ps.setBoolean(2, note.isAbsent());
            ps.setString(3, note.getCommentaire());
            ps.setString(4, note.getSource());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(6, note.getEtudiant().getId());
            ps.setLong(7, note.getEvaluation().getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la sauvegarde de la note", e);
        }
    }

    /**
     * Insère une liste de notes en batch (import CSV).
     */
    public int saveAllBatch(List<Note> notes) throws DaoException {
        int total = 0;
        try (Connection conn = ConnectionManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(UPSERT_NOTE)) {
                for (Note note : notes) {
                    ps.setObject(1, note.getValeurNote());
                    ps.setBoolean(2, note.isAbsent());
                    ps.setString(3, note.getCommentaire());
                    ps.setString(4, note.getSource());
                    ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setLong(6, note.getEtudiant().getId());
                    ps.setLong(7, note.getEvaluation().getId());
                    ps.addBatch();
                }
                int[] results = ps.executeBatch();
                conn.commit();
                for (int r : results) total += (r > 0 ? 1 : 0);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de l'import en batch des notes", e);
        }
        return total;
    }

    /**
     * Retourne toutes les notes d'une évaluation donnée.
     */
    public List<Note> findByEvaluation(long evaluationId) throws DaoException {
        List<Note> notes = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_EVALUATION)) {

            ps.setLong(1, evaluationId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                notes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération des notes", e);
        }
        return notes;
    }

    /**
     * Retourne toutes les notes d'un étudiant pour un carnet donné.
     */
    public List<Note> findByEtudiantAndCarnet(long etudiantId, long carnetId) throws DaoException {
        List<Note> notes = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ETUDIANT_AND_CARNET)) {

            ps.setLong(1, etudiantId);
            ps.setLong(2, carnetId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                notes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération des notes de l'étudiant", e);
        }
        return notes;
    }

    /**
     * Supprime toutes les notes d'une évaluation.
     */
    public int deleteByEvaluation(long evaluationId) throws DaoException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_EVALUATION)) {
            ps.setLong(1, evaluationId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la suppression des notes", e);
        }
    }

    /**
     * Calcule la moyenne d'une évaluation (hors absents).
     */
    public Float computeAverage(long evaluationId) throws DaoException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(AVG_BY_EVALUATION)) {
            ps.setLong(1, evaluationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                float avg = rs.getFloat(1);
                return rs.wasNull() ? null : avg;
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException("Erreur lors du calcul de la moyenne", e);
        }
    }

    // ── Mapping ResultSet → Note ───────────────────────────────────────────────
    private Note mapRow(ResultSet rs) throws SQLException {
        Note note = new Note();
        note.setValeurNote(rs.getObject("valeur_note") != null ? rs.getFloat("valeur_note") : null);
        note.setAbsent(rs.getBoolean("absent"));
        note.setCommentaire(rs.getString("commentaire"));
        note.setSource(rs.getString("source"));

        Timestamp ts = rs.getTimestamp("date_saisie");
        if (ts != null) note.setDateSaisie(ts.toLocalDateTime());

        // On crée des objets Etudiant et Evaluation avec seulement leur ID
        // pour éviter les jointures supplémentaires (les services feront les résolutions si besoin)
        Etudiant e = new Etudiant(); // setId n'est pas exposé → on utilise une méthode package ou on passe par JPA
        // Note : si vous utilisez JPA/Hibernate, cette DAO JDBC coexiste avec les repositories JPA.
        // Ici on stocke les IDs bruts dans des wrappers légers.
        note.setSource(rs.getString("source"));
        return note;
    }
}