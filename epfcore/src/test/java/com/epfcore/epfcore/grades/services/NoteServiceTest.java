package com.epfcore.epfcore.grades.services;

import com.epfcore.epfcore.grades.entities.CarnetDeNotes;
import com.epfcore.epfcore.grades.entities.Evaluation;
import com.epfcore.epfcore.grades.entities.Module;
import com.epfcore.epfcore.grades.entities.Note;
import com.epfcore.epfcore.grades.entities.UniteEnseignement;
import com.epfcore.epfcore.student.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private NoteService noteService;

    @Mock
    private TypedQuery query;

    private CarnetDeNotes carnet;
    private Evaluation evaluation;
    private Student etudiant;

    @BeforeEach
    void setUp() {
        carnet = new CarnetDeNotes();
        carnet.setId(1L);
        carnet.setIntitule("Test Carnet");
        carnet.setStatut("BROUILLON");

        evaluation = new Evaluation("Eval 1", "TP", 1.0f, 20.0f, carnet);
        evaluation.setId(1L);

        etudiant = new Student();
        etudiant.setId(1L);
        etudiant.setStudentNumber("12345");
    }

    @Test
    void creerCarnet() {
        UniteEnseignement ue = new UniteEnseignement();
        Module module = new Module();
        
        when(em.find(UniteEnseignement.class, 1L)).thenReturn(ue);
        when(em.find(Module.class, 1L)).thenReturn(module);

        CarnetDeNotes result = noteService.creerCarnet("Intitule", "2023-2024", 1L, 1L);

        assertNotNull(result);
        assertEquals("Intitule", result.getIntitule());
        assertEquals("2023-2024", result.getAnneeAcademique());
        assertEquals("BROUILLON", result.getStatut());
        assertEquals(ue, result.getUniteEnseignement());
        assertEquals(module, result.getModule());
        verify(em, times(1)).persist(any(CarnetDeNotes.class));
    }

    @Test
    void findAllCarnets() {
        when(em.createQuery(anyString(), eq(CarnetDeNotes.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(carnet));

        List<CarnetDeNotes> result = noteService.findAllCarnets();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void publierCarnet() {
        when(em.find(CarnetDeNotes.class, 1L)).thenReturn(carnet);

        CarnetDeNotes result = noteService.publierCarnet(1L);

        assertEquals("PUBLIE", result.getStatut());
        assertNotNull(result.getDatePublication());
    }

    @Test
    void ajouterEvaluation() {
        when(em.find(CarnetDeNotes.class, 1L)).thenReturn(carnet);

        Evaluation result = noteService.ajouterEvaluation(1L, "TP1", "TP", 1.5f, 20.0f);

        assertNotNull(result);
        assertEquals("TP1", result.getIntitule());
        verify(em, times(1)).persist(any(Evaluation.class));
    }

    @Test
    void saisirNote() {
        when(em.find(Evaluation.class, 1L)).thenReturn(evaluation);
        when(em.find(Student.class, 1L)).thenReturn(etudiant);
        when(em.createQuery(anyString(), eq(Note.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        Note result = noteService.saisirNote(1L, 1L, 15.0f, "Bien");

        assertNotNull(result);
        assertEquals(15.0f, result.getValeurNote());
        assertEquals("Bien", result.getCommentaire());
        verify(em, times(1)).persist(any(Note.class));
    }

    @Test
    void calculerMoyenneEtudiant() {
        when(em.createQuery(anyString(), eq(Object[].class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        
        Object[] row1 = new Object[]{10.0f, 1.0f};
        Object[] row2 = new Object[]{15.0f, 2.0f};
        when(query.getResultList()).thenReturn(List.of(row1, row2));

        Float moyenne = noteService.calculerMoyenneEtudiant(1L, 1L);

        assertNotNull(moyenne);
        assertEquals(13.33f, moyenne, 0.01f);
    }
}