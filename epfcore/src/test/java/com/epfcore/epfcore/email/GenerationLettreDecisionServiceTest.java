package com.epfcore.epfcore.email;

import com.epfcore.epfcore.formulaireInscription.entity.DecisionAdmission;
import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import com.epfcore.epfcore.security.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class GenerationLettreDecisionServiceTest {

    @InjectMocks
    private GenerationLettreDecisionService generationLettreDecisionService;

    private FormulaireInscription formulaire;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setLastname("Test");
        user.setFirstname("User");

        formulaire = new FormulaireInscription();
        formulaire.setUser(user);
        formulaire.setDecisionAdmission(DecisionAdmission.ADMIS);
        formulaire.setProgrammeChoisi("Programme Test");
    }

    @Test
    void generate() {
        byte[] result = generationLettreDecisionService.generate(formulaire);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}