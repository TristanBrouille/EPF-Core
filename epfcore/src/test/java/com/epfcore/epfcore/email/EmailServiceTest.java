package com.epfcore.epfcore.email;

import com.epfcore.epfcore.entretienCandidature.entity.EntretienCandidature;
import com.epfcore.epfcore.formulaireInscription.entity.DecisionAdmission;
import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import com.epfcore.epfcore.security.domain.User;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private GenerationLettreDecisionService lettreService;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private MimeMessage mimeMessage;

    private FormulaireInscription formulaire;
    private EntretienCandidature entretien;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@test.com");
        user.setFirstname("Test");

        formulaire = new FormulaireInscription();
        formulaire.setUser(user);
        formulaire.setDecisionAdmission(DecisionAdmission.ADMIS);

        entretien = new EntretienCandidature();
        entretien.setFormulaire(formulaire);
        entretien.setDateHeure(java.time.LocalDateTime.now());
    }

    @Test
    void sendDecisionEmail() throws Exception {
        when(lettreService.generate(formulaire)).thenReturn(new byte[0]);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendDecisionEmail(formulaire);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendEntretienEmail() throws Exception {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendEntretienEmail(entretien);

        verify(mailSender, times(1)).send(mimeMessage);
    }
}