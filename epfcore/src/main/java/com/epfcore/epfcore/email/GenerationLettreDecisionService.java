package com.epfcore.epfcore.email;

import com.epfcore.epfcore.formulaireInscription.entity.DecisionAdmission;
import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Base64;

@Service
public class GenerationLettreDecisionService {

    public byte[] generate(FormulaireInscription formulaire) {
        try {
            String html = buildHtml(formulaire);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur génération lettre de décision", e);
        }
    }

    private String buildHtml(FormulaireInscription formulaire) {
        String logoBase64 = loadImageAsBase64("static/images/logo_epf2.png");

        String nom = formulaire.getUser().getLastname().toUpperCase();
        String prenom = formulaire.getUser().getFirstname();
        String programme = formulaire.getProgrammeChoisi() != null ? formulaire.getProgrammeChoisi() : "";
        String campus = formulaire.getCampus() != null ? formulaire.getCampus().getVille() : "Cachan";

        boolean admis = formulaire.getDecisionAdmission() == DecisionAdmission.ADMIS;

        String[] mois = {"janvier", "février", "mars", "avril", "mai", "juin",
                "juillet", "août", "septembre", "octobre", "novembre", "décembre"};
        LocalDate today = LocalDate.now();
        String dateFormatee = today.getDayOfMonth() + " " + mois[today.getMonthValue() - 1] + " " + today.getYear();

        String decisionBox = admis
                ? "<div class=\"decision-admis\">&#10003;&#160;&#160;Votre candidature est ACCEPTÉE</div>"
                : "<div class=\"decision-refuse\">&#10007;&#160;&#160;Votre candidature n'a pas été retenue</div>";

        String corpsIntro = admis
                ? "Après examen de votre dossier et entretien avec notre jury d'admission, nous avons le plaisir de vous informer de la décision suivante&#160;:"
                : "Après examen attentif de votre dossier et entretien avec notre jury d'admission, nous sommes au regret de vous informer de la décision suivante&#160;:";

        String corpsSuite = admis
                ? "<div class=\"corps\">Afin de finaliser votre inscription, vous êtes invité(e) à vous connecter à votre espace personnel sur le portail EPF. Votre compte aura été mis à jour avec les droits d'accès étudiant, vous permettant d'accéder à l'ensemble des services et ressources mis à votre disposition.<br/><br/>Nous vous invitons également à prendre connaissance des documents de rentrée qui vous seront communiqués prochainement par email et sur votre espace étudiant.<br/><br/>Nous vous souhaitons la bienvenue au sein de l'EPF et vous adressons nos cordiales salutations.</div>"
                : "<div class=\"corps\">Nous vous prions d'agréer, Madame, Monsieur, l'expression de nos salutations distinguées.</div>";

        return """
                <html>
                <head>
                    <meta charset="UTF-8"/>
                    <style>
                        @page { margin: 40px 50px; }
                        body { font-family: Arial, sans-serif; font-size: 13px; color: #000; margin: 0; padding: 0; }
                        table { width: 100%%; border-collapse: collapse; }
                        td { vertical-align: top; }
                        .address { text-align: right; font-size: 11px; line-height: 1.8; }
                        .title { text-align: center; font-size: 18px; font-weight: bold; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 6px; }
                        .ref { text-align: center; font-size: 11px; color: #888; margin-bottom: 40px; }
                        .date-lieu { text-align: right; font-size: 12px; margin-bottom: 30px; }
                        .destinataire { margin-bottom: 30px; font-size: 13px; }
                        .objet { margin-bottom: 25px; font-size: 13px; }
                        .corps { font-size: 13px; line-height: 1.8; margin-bottom: 20px; text-align: justify; }
                        .decision-admis { background: #e8f5e9; border-left: 5px solid #2e7d32; padding: 15px 20px; margin: 25px 0; font-size: 15px; font-weight: bold; color: #2e7d32; }
                        .decision-refuse { background: #ffebee; border-left: 5px solid #c62828; padding: 15px 20px; margin: 25px 0; font-size: 15px; font-weight: bold; color: #c62828; }
                        .info-table td { padding: 5px 10px; }
                        .info-table td:first-child { font-weight: bold; width: 180px; color: #555; }
                        .signataire { text-align: right; margin-top: 60px; font-size: 13px; }
                        .footer { margin-top: 60px; border-top: 1px solid #ccc; padding-top: 10px; text-align: center; font-size: 10px; color: #999; }
                    </style>
                </head>
                <body>
                    <table style="margin-bottom: 40px;">
                        <tr>
                            <td><img src="%s" alt="Logo EPF" style="height:70px;"/></td>
                            <td class="address">
                                FONDATION EPF<br/>
                                55 AVENUE DU PRÉSIDENT WILSON<br/>
                                94230 CACHAN<br/>
                                www.epf.fr
                            </td>
                        </tr>
                    </table>

                    <div class="title">Lettre de décision d'admission</div>
                    <div class="ref">Réf. : ADM-%d-%05d</div>

                    <div class="date-lieu">Cachan, le %s</div>

                    <div class="destinataire"><strong>%s %s</strong></div>

                    <div class="objet"><strong>Objet&#160;:</strong> Décision d'admission — Programme %s — Campus de %s</div>

                    <div class="corps">
                        Madame, Monsieur,<br/><br/>
                        Nous avons bien pris connaissance de votre candidature au programme <strong>%s</strong>
                        au sein de l'EPF École d'Ingénieur-e-s, campus de <strong>%s</strong>.<br/><br/>
                        %s
                    </div>

                    %s

                    <table class="info-table" style="margin: 20px 0;">
                        <tr><td>Candidat&#160;:</td><td>%s %s</td></tr>
                        <tr><td>Programme&#160;:</td><td>%s</td></tr>
                        <tr><td>Campus&#160;:</td><td>%s</td></tr>
                    </table>

                    %s

                    <div class="signataire">
                        <strong>Emmanuel DUFLOS</strong><br/>
                        Directeur général de l'EPF
                    </div>

                    <div class="footer">
                        EPF École d'Ingénieur-e-s — 55 avenue du Président Wilson — 94230 Cachan — www.epf.fr
                    </div>
                </body>
                </html>
                """.formatted(
                        logoBase64,
                        today.getYear(), formulaire.getId(),
                        dateFormatee,
                        nom, prenom,
                        programme, campus,
                        programme, campus,
                        corpsIntro,
                        decisionBox,
                        nom, prenom, programme, campus,
                        corpsSuite
                );
    }

    private String loadImageAsBase64(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            byte[] bytes = resource.getContentAsByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Image introuvable : " + path, e);
        }
    }
}
