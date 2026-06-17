package com.epfcore.epfcore.email;

import com.epfcore.epfcore.formulaireInscription.entity.DecisionAdmission;
import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final GenerationLettreDecisionService lettreService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String fromName;

    public EmailService(JavaMailSender mailSender, GenerationLettreDecisionService lettreService) {
        this.mailSender = mailSender;
        this.lettreService = lettreService;
    }

    public void sendDecisionEmail(FormulaireInscription formulaire) throws Exception {
        boolean admis = formulaire.getDecisionAdmission() == DecisionAdmission.ADMIS;
        String toEmail = formulaire.getUser().getEmail();
        String prenom = formulaire.getUser().getFirstname();

        String subject = admis
                ? "Votre candidature EPF a été acceptée"
                : "Résultat de votre candidature EPF";

        String body = admis
                ? String.format(
                    "Bonjour %s,<br/><br/>"
                    + "Nous avons le plaisir de vous informer que votre candidature a été <strong>acceptée</strong>.<br/>"
                    + "Veuillez trouver ci-joint votre lettre de décision officielle.<br/><br/>"
                    + "Cordialement,<br/>Le service des admissions EPF", prenom)
                : String.format(
                    "Bonjour %s,<br/><br/>"
                    + "Nous avons le regret de vous informer que votre candidature n'a pas été retenue.<br/>"
                    + "Veuillez trouver ci-joint votre lettre de décision.<br/><br/>"
                    + "Cordialement,<br/>Le service des admissions EPF", prenom);

        byte[] pdf = lettreService.generate(formulaire);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromName + " <" + fromEmail + ">");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.addAttachment("Lettre_decision_EPF.pdf", new ByteArrayResource(pdf));

        mailSender.send(message);
    }
}
