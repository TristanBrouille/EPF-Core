package com.epfcore.epfcore.document.service;


import com.epfcore.epfcore.document.entity.Document;
import com.epfcore.epfcore.document.entity.DocumentRequest;
import com.epfcore.epfcore.document.repository.DocumentRepository;
import com.epfcore.epfcore.document.repository.DocumentRequestRepository;
import com.epfcore.epfcore.student.entity.Student;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class GenerationCertificateService {

    private final DocumentRepository documentRepository;
    private final DocumentRequestRepository documentRequestRepository;

    public GenerationCertificateService(DocumentRepository documentRepository,
                                        DocumentRequestRepository documentRequestRepository) {
        this.documentRepository = documentRepository;
        this.documentRequestRepository = documentRequestRepository;
    }

    // -------------------------------------------------------------------------
    // Point d'entrée principal
    // -------------------------------------------------------------------------

    public byte[] generateFromRequest(Integer requestId) {
        // 1. Récupérer la demande
        DocumentRequest request = documentRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("DocumentRequest not found: " + requestId));

        Student student = request.getStudent();

        // 2. Générer le PDF
        byte[] pdfBytes = generateStudentCertificateHtml(student);

        // 3. Sauvegarder le Document en base
        Document document = new Document();
        document.setStudent(student);
        document.setRequest(request);
        document.setDocumentType(DocumentRequest.DocumentType.CERTIFICATE);
        document.setAcademicYear(student.getAcademicYear());
        document.setCreationDate(LocalDateTime.now());
        documentRepository.save(document);

        // 4. Passer la demande à APPROVED
        request.setStatus(DocumentRequest.DocumentRequestStatus.APPROVED);
        request.setProcessingDate(LocalDateTime.now());
        documentRequestRepository.save(request);

        return pdfBytes;
    }

    // -------------------------------------------------------------------------
    // Génération PDF
    // -------------------------------------------------------------------------

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
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    // -------------------------------------------------------------------------
    // Construction du HTML
    // -------------------------------------------------------------------------

    private String buildHtml(Student student) {
        String logoBase64 = loadImageAsBase64();

        String nom = student.getUser().getLastname().toUpperCase();
        String prenom = student.getUser().getFirstname();
        String address = student.getAddress();
        LocalDate birthDate = student.getUser().getBirthDate();
        String birthDateFormatee = birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String campus = student.getCampus();
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
                                <img src="%s" alt="Logo EPF" />
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
                        logoBase64,
                        anneeUniversitaire,
                        nom, prenom,
                        birthDateFormatee,
                        campus,
                        address,
                        anneeUniversitaire,
                        major,
                        campus, dateComplete);
    }

    // -------------------------------------------------------------------------
    // Chargement du logo en base64
    // -------------------------------------------------------------------------

    private String loadImageAsBase64() {
        try (InputStream is = getClass().getResourceAsStream("/static/images/logo_epf2.png")) {
            if (is == null) {
                throw new RuntimeException("Logo introuvable : /static/images/logo_epf2.png");
            }
            byte[] bytes = is.readAllBytes();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement du logo", e);
        }
    }
}