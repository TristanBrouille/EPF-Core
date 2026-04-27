package com.epfcore.epfcore.student.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import com.epfcore.epfcore.student.entity.Student;

import java.io.ByteArrayOutputStream;

@Service
public class GenerationPdfService {

    public byte[] generateStudentPdfHtml(Student student) {

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

    String nom = student.getUser().getLastname();
    String prenom = student.getUser().getFirstname();
    String email = student.getUser().getEmail();
    String phone = student.getPhone();
    String numero = student.getStudentNumber();
    String major = student.getMajor();
    String year = student.getAcademicYear();

    return """
    <html>
    <head>
        <style>
            body { font-family: Arial; padding: 20px; }

            .card { border: 1px solid #ddd; padding: 15px; margin-bottom: 15px; border-radius: 10px; }

            .title { text-align: center; font-size: 22px; font-weight: bold; }

            .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }

            .label { font-weight: bold; color: #555; }
        </style>
    </head>

    <body>

        <div class="title">FICHE ÉTUDIANT</div>

        <div class="card">
            <h3>Informations</h3>
            <div class="grid">
                <div><span class="label">Nom:</span> %s</div>
                <div><span class="label">Prénom:</span> %s</div>
                <div><span class="label">Email:</span> %s</div>
                <div><span class="label">Téléphone:</span> %s</div>
            </div>
        </div>

        <div class="card">
            <h3>Scolarité</h3>
            <p><b>Numéro:</b> %s</p>
            <p><b>Filière:</b> %s</p>
            <p><b>Année:</b> %s</p>
        </div>

    </body>
    </html>
    """.formatted(nom, prenom, email, phone, numero, major, year);
}
}