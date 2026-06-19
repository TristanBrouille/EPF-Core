package com.epfcore.epfcore.entretienCandidature.service;

import com.epfcore.epfcore.campus.entity.Campus;
import com.epfcore.epfcore.campus.repository.CampusRepository;
import com.epfcore.epfcore.email.EmailService;
import com.epfcore.epfcore.entretienCandidature.dto.EntretienCandidatureDTO;
import com.epfcore.epfcore.entretienCandidature.entity.EntretienCandidature;
import com.epfcore.epfcore.entretienCandidature.repository.EntretienCandidatureRepository;
import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import com.epfcore.epfcore.formulaireInscription.repository.FormulaireInscriptionRepository;
import com.epfcore.epfcore.security.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntretienCandidatureServiceTest {

    @Mock
    private EntretienCandidatureRepository entretienRepository;

    @Mock
    private FormulaireInscriptionRepository formulaireRepository;

    @Mock
    private CampusRepository campusRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EntretienCandidatureService entretienCandidatureService;

    private EntretienCandidatureDTO entretienDTO;
    private FormulaireInscription formulaire;
    private User user;
    private Campus campus;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        formulaire = new FormulaireInscription();
        formulaire.setId(1L);
        formulaire.setUser(user);

        campus = new Campus();
        campus.setVille("Testville");

        entretienDTO = new EntretienCandidatureDTO(null, 1L, null, null, null, LocalDateTime.now(), "Testville", null, null, null, null, null, null, null, null);
    }

    @Test
    void create() throws Exception {
        when(formulaireRepository.findById(1L)).thenReturn(Optional.of(formulaire));
        when(campusRepository.findByVille("Testville")).thenReturn(Optional.of(campus));
        when(entretienRepository.save(any(EntretienCandidature.class))).thenAnswer(i -> i.getArguments()[0]);
        doNothing().when(emailService).sendEntretienEmail(any());

        EntretienCandidatureDTO result = entretienCandidatureService.create(entretienDTO);

        assertNotNull(result);
        verify(entretienRepository, times(1)).save(any(EntretienCandidature.class));
        verify(emailService, times(1)).sendEntretienEmail(any());
    }
}