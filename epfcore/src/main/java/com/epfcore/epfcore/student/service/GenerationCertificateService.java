package com.epfcore.epfcore.student.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import com.epfcore.epfcore.student.entity.Student;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class GenerationCertificateService {

    public byte[] generateStudentCertificateHtml(Student student) {
        try {
            String html = buildHtml(student);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur PDF HTML", e);
        }
    }

    private String buildHtml(Student student) {
        String nom = student.getUser().getLastname().toUpperCase();
        String prenom = student.getUser().getFirstname();
        String address = student.getAddress();
        LocalDate birthDate = student.getUser().getBirthDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String birthDateFormatee = birthDate.format(formatter);
        String campus = student.getCampus();
        String academicYear = student.getAcademicYear();
        String major = student.getMajor();
        LocalDate enrollmentDate = student.getEnrollmentDate();

        int year = enrollmentDate.getYear();
        String anneeUniversitaire = year + " - " + (year + 1);

        String[] jours = { "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi", "dimanche" };
        String jourSemaine = jours[enrollmentDate.getDayOfWeek().getValue() - 1];
        String[] mois = { "janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre",
                "octobre", "novembre", "décembre" };
        String dateComplete = jourSemaine + " " + enrollmentDate.getDayOfMonth() + " "
                + mois[enrollmentDate.getMonthValue() - 1] + " " + enrollmentDate.getYear();

        return """
                <html>
                <head>
                    <style>
                        @page { margin: 40px 50px; }
                        body {
                            font-family: Arial, sans-serif;
                            font-size: 13px;
                            color: #000;
                            padding: 0;
                            margin: 0;
                        }
                        table { width: 100%%; border-collapse: collapse; }
                        td { vertical-align: top; }
                        .title {
                            text-align: center;
                            font-size: 22px;
                            margin-bottom: 8px;
                        }
                        .subtitle {
                            text-align: center;
                            font-size: 16px;
                            margin-bottom: 40px;
                        }
                        .intro {
                            margin-bottom: 20px;
                            font-size: 13px;
                        }
                        .info-label {
                            width: 130px;
                        }
                        .info-value {
                            font-weight: bold;
                        }
                        .formation {
                            text-align: center;
                            font-weight: bold;
                            font-size: 14px;
                            margin-top: 30px;
                            margin-bottom: 40px;
                        }
                        .signature-block {
                            text-align: center;
                            margin-top: 60px;
                        }
                    </style>
                </head>
                <body>

                    <table style="margin-bottom: 40px;">
                        <tr>
                            <td>
                                <strong>epf</strong><br/>
                                <span style="font-size:10px;">ÉCOLE D'INGÉNIEUR·E·S<br/><em>Creating the future together</em></span>
                            </td>
                            <td style="text-align:right; font-size:12px;">
                                FONDATION EPF<br/>
                                55 AVENUE DU PRÉSIDENT WILSON<br/>
                                94230 CACHAN
                            </td>
                        </tr>
                    </table>

                    <div class="title">Certificat de scolarité</div>
                    <div class="subtitle">Année universitaire : %s</div>

                    <div class="intro">
                        Je soussigné, Emmanuel DUFLOS Directeur général de l'EPF, certifie que
                    </div>

                    <table style="margin-bottom: 12px;">
                        <tr>
                            <td class="info-label">L'élève :</td>
                            <td class="info-value">%s %s</td>
                        </tr>
                    </table>

                    <table style="margin-bottom: 12px;">
                        <tr>
                            <td class="info-label">Née le :</td>
                            <td>%s &#160;&#160;&#160;&#160;&#160; à : &#160;&#160;&#160;&#160;&#160; %s</td>
                        </tr>
                    </table>

                    <table style="margin-bottom: 12px;">
                        <tr>
                            <td class="info-label">Résidant à :</td>
                            <td>%s</td>
                        </tr>
                    </table>

                    <div class="intro" style="margin-top:20px;">
                        est inscrit(e) sur les registres de l'Etablissement pour l'année scolaire %s en
                    </div>

                    <div class="formation">%s</div>

                    <table style="margin-top: 60px;">
                        <tr>
                            <td style="font-size:13px;">Fait à %s, le %s</td>
                            <td style="text-align:right; font-size:11px;">
                                EPF ÉCOLE D'INGÉNIEUR·E·S<br/>
                                55, Avenue du Président Wilson<br/>
                                94230 CACHAN – FRANCE<br/>
                                Tél. 01 41 13 01 51
                            </td>
                        </tr>
                    </table>

                    <div class="signature-block">
                        Emmanuel DUFLOS<br/>
                        Directeur général de l'EPF
                    </div>

                </body>
                </html>
                """
                .formatted(
                        anneeUniversitaire,
                        nom, prenom,
                        birthDateFormatee,
                        campus,
                        address,
                        anneeUniversitaire,
                        major,
                        campus, dateComplete);
    }
}