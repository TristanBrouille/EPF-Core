package com.epfcore.epfcore.document.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.epfcore.epfcore.document.entity.Document;
import com.epfcore.epfcore.document.entity.DocumentRequest;
import com.epfcore.epfcore.document.repository.DocumentRepository;
import com.epfcore.epfcore.document.repository.DocumentRequestRepository;
import com.epfcore.epfcore.student.entity.Student;
import com.epfcore.epfcore.student.repository.StudentRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;

@Service
public class GenerationPdfService {

    private final DocumentRepository documentRepository;
    private final DocumentRequestRepository documentRequestRepository;
    private final StudentRepository studentRepository;

    public GenerationPdfService(DocumentRepository documentRepository,
                                        DocumentRequestRepository documentRequestRepository,
                                        StudentRepository studentRepository) {
        this.documentRepository = documentRepository;
        this.documentRequestRepository = documentRequestRepository;
        this.studentRepository = studentRepository;
    }

    public byte[] generateFromRequest(Integer studentId) {
 
        List<Document> existing = documentRepository.findByStudentId(studentId);
        Optional<Document> existingCertificate = existing.stream()
                .filter(d -> d.getDocumentType() == DocumentRequest.DocumentType.INFOS)
                .findFirst();
 
        if (existingCertificate.isPresent()) {
            Student student = existingCertificate.get().getStudent();
            return generateStudentPdfInfosHtml(student);
        }
 
        Student student = studentRepository.findById(Long.valueOf(studentId))
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
 
        DocumentRequest request = new DocumentRequest();
        request.setStudent(student);
        request.setDocumentType(DocumentRequest.DocumentType.INFOS);
        request.setStatus(DocumentRequest.DocumentRequestStatus.APPROVED);
        request.setCreationDate(LocalDateTime.now());
        request.setProcessingDate(LocalDateTime.now());
        DocumentRequest savedRequest = documentRequestRepository.save(request);
 
        Document document = new Document();
        document.setStudent(student);
        document.setRequest(savedRequest);
        document.setDocumentType(DocumentRequest.DocumentType.INFOS);
        document.setAcademicYear(student.getAcademicYear());
        document.setCreationDate(LocalDateTime.now());
        documentRepository.save(document);

        return generateStudentPdfInfosHtml(student);
    }
 
    public byte[] generateStudentPdfInfosHtml(Student student) {
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

    String nom = student.getUser().getLastname();
    String prenom = student.getUser().getFirstname();
    String email = student.getUser().getEmail();
    LocalDate birthDate = student.getUser().getBirthDate();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String birthDateFormatee = birthDate.format(formatter);
    String nationality = student.getNationality();
    String phone = student.getPhone();
    String numeroFormate = phone.replaceAll("(.{2})", "$1 ").trim();
    String numero = student.getStudentNumber();
    String major = student.getMajor();
    String address = student.getAddress();
    String lastDegree = student.getLastDegree();
    LocalDate enrollmentDate = student.getEnrollmentDate();
    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String EnrollmentDateFormatee = enrollmentDate.format(formatter1);
    Boolean bourse = student.getScholarship();
    String bourseFormatee = Boolean.TRUE.equals(bourse) ? "Boursier" : "Non boursier";
    String campus = student.getCampus();
    String academicYear= student.getAcademicYear();

    return """
    <html>
    <head>
        <style>
            body { font-family: Arial; padding: 20px; }

            .card { border: 1px solid #ddd; padding: 15px; margin-bottom: 30px; border-radius: 10px; }

            .title { text-align: center; font-size: 22px; font-weight: bold;margin-bottom: 40px; }

            .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }

            .label { font-weight: bold; color: #555; }
        </style>
    </head>

    <body>

        <div class="title">FICHE ÉTUDIANT</div>

        <div class="card">
            <div class="grid">
                 <p><b>Bourse:</b> %s</p>
                 <p><b>Campus:</b> %s</p>
                 <p><b>Année:</b> %s</p>
                
            </div>
        </div>

        <div class="card">
            <h2>Informations</h2>
            <div class="grid">
                 <p><b>Nom:</b> %s</p>
                 <p><b>Prénom:</b> %s</p>
                 <p><b>Email:</b> %s</p>
                 <p><b>Nationalité:</b> %s</p>
                 <p><b>Date d'anniversaire: </b> %s</p>
                 <p><b>Téléphone:</b> %s</p>
                 <p><b>Adresse:</b> %s</p>
                
            </div>
        </div>

        <div class="card">
            <h2>Scolarité</h2>
            <p><b>Numéro étudiant:</b> %s</p>
            <p><b>Filière:</b> %s</p>
            <p><b>Dernier diplôme:</b> %s</p>
            <p><b>Date inscription:</b> %s</p>
        </div>

    </body>
    </html>
    """.formatted(bourseFormatee, campus, academicYear, nom, prenom, email, nationality,birthDateFormatee,numeroFormate, address,  numero, major, lastDegree, EnrollmentDateFormatee);
}
    
}
