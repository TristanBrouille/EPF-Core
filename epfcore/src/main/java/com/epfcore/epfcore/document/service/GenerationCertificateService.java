package com.epfcore.epfcore.document.service;

import com.epfcore.epfcore.document.entity.Document;
import com.epfcore.epfcore.document.repository.DocumentRepository;
import com.epfcore.epfcore.student.entity.Student;
import com.epfcore.epfcore.student.repository.StudentRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class GenerationCertificateService {

    private final DocumentRepository documentRepository;
    private final StudentRepository studentRepository;

    public GenerationCertificateService(DocumentRepository documentRepository,
            StudentRepository studentRepository) {
        this.documentRepository = documentRepository;
        this.studentRepository = studentRepository;
    }

    public byte[] generateFromRequest(Integer studentId) {
        Student student = studentRepository.findById(Long.valueOf(studentId))
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        Integer userId = Math.toIntExact(student.getUser().getId());

        List<Document> existing = documentRepository.findByUserId(userId);
        Optional<Document> existingCertificate = existing.stream()
                .filter(d -> d.getDocumentType() == Document.DocumentType.CERTIFICATE)
                .findFirst();

        if (existingCertificate.isPresent()) {
            String fileData = existingCertificate.get().getFileUrl();
            if (fileData != null) {
                return Base64.getDecoder().decode(fileData);
            }
            return generateStudentCertificateHtml(student);
        }

        byte[] pdf = generateStudentCertificateHtml(student);

        Document document = new Document();
        document.setUserId(userId);
        document.setDocumentType(Document.DocumentType.CERTIFICATE);
        document.setStatus(Document.DocumentStatus.APPROVED);
        document.setCreationDate(LocalDateTime.now());
        document.setProcessingDate(LocalDateTime.now());
        document.setFileUrl(Base64.getEncoder().encodeToString(pdf));
        documentRepository.save(document);

        return pdf;
    }

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

    private String buildHtml(Student student) {
        String logoBase64 = loadImageAsBase64("static/images/logo_epf2.png", "logo");
        String signatureBase64 = loadImageAsBase64("static/images/Signature.png", "signature");
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
                            <td>%s &#160;&#160;&#160;&#160;&#160;</td>
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

                    <div class="formation">Formation Ingénieur Généraliste %s</div>

                    <table style="margin-top: 60px;">
                        <tr>
                            <td style="font-size:13px;">Fait à %s, le %s</td>
                            <td style="text-align:right; font-size:11px;">
                               <img src="%s" alt="Signature EPF" />
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
                        address,
                        anneeUniversitaire,
                        major,
                        campus, dateComplete,
                        signatureBase64);

    }

    private String loadImageAsBase64(String path, String label) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            byte[] bytes = resource.getContentAsByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Image introuvable : " + path + " (" + label + ")", e);
        }
    }

}