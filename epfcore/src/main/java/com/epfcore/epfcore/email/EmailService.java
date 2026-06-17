package com.epfcore.epfcore.email;

import com.epfcore.epfcore.entretienCandidature.entity.EntretienCandidature;
import com.epfcore.epfcore.entretienCandidature.entity.TypeEntretien;
import com.epfcore.epfcore.formulaireInscription.entity.DecisionAdmission;
import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    public void sendEntretienEmail(EntretienCandidature entretien) throws Exception {
        String toEmail = entretien.getFormulaire().getUser().getEmail();
        String prenom = entretien.getFormulaire().getUser().getFirstname();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy 'à' HH'h'mm", Locale.FRENCH);
        String dateFormatee = entretien.getDateHeure().format(formatter);

        String typeLabel;
        String details;
        TypeEntretien type = entretien.getTypeEntretien();
        if (type == TypeEntretien.VISIO) {
            typeLabel = "Visioconférence";
            details = "Lien de connexion : <a href=\"" + entretien.getLienVisio() + "\">" + entretien.getLienVisio() + "</a>";
        } else if (type == TypeEntretien.TELEPHONE) {
            typeLabel = "Téléphone";
            details = "Vous serez contacté(e) par téléphone.";
        } else {
            typeLabel = "Présentiel";
            String lieu = entretien.getCampus() != null ? entretien.getCampus().getVille() : "";
            String salle = entretien.getSalle() != null ? " — Salle : " + entretien.getSalle() : "";
            details = "Campus : " + lieu + salle;
        }

        String body = String.format(
            "Bonjour %s,<br/><br/>"
            + "Nous avons le plaisir de vous informer qu'un entretien de candidature a été planifié pour votre dossier.<br/><br/>"
            + "<strong>Détails de l'entretien :</strong><br/>"
            + "<table style=\"border-collapse:collapse; margin-top:10px;\">"
            + "<tr><td style=\"padding:4px 12px 4px 0; font-weight:bold;\">Date et heure :</td><td>%s</td></tr>"
            + "<tr><td style=\"padding:4px 12px 4px 0; font-weight:bold;\">Type :</td><td>%s</td></tr>"
            + "<tr><td style=\"padding:4px 12px 4px 0; font-weight:bold;\">Lieu :</td><td>%s</td></tr>"
            + "</table><br/>"
            + "Merci de vous assurer d'être disponible à cette date.<br/><br/>"
            + "Cordialement,<br/>Le service des admissions EPF",
            prenom, dateFormatee, typeLabel, details);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromName + " <" + fromEmail + ">");
        helper.setTo(toEmail);
        helper.setSubject("Convocation à un entretien de candidature EPF");
        helper.setText(body, true);

        mailSender.send(message);
    }
}
