package com.epfcore.epfcore.formulaireInscription.service;

import com.epfcore.epfcore.campus.repository.CampusRepository;
import com.epfcore.epfcore.documentFormulaire.repository.DocumentFormulaireRepository;
import com.epfcore.epfcore.documentFormulaire.storage.StorageService;
import com.epfcore.epfcore.email.EmailService;
import com.epfcore.epfcore.formulaireInscription.dto.FormulaireInscriptionDTO;
import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import com.epfcore.epfcore.formulaireInscription.repository.FormulaireInscriptionRepository;
import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.infrastructure.UserJpaRepository;
import com.epfcore.epfcore.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormulaireInscriptionServiceTest {

    @Mock
    private FormulaireInscriptionRepository formulaireRepository;

    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private FormulaireInscriptionService formulaireInscriptionService;

    private FormulaireInscriptionDTO formulaireDTO;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        formulaireDTO = new FormulaireInscriptionDTO(null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null);
    }

    @Test
    void save() {
        when(userRepository.ofEmail("test@test.com")).thenReturn(user);
        when(formulaireRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(formulaireRepository.save(any(FormulaireInscription.class))).thenAnswer(i -> {
            FormulaireInscription f = i.getArgument(0);
            f.setId(1L);
            return f;
        });

        FormulaireInscriptionDTO result = formulaireInscriptionService.save(formulaireDTO, "test@test.com");

        assertNotNull(result);
        verify(formulaireRepository, times(1)).save(any(FormulaireInscription.class));
    }
}