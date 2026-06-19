package com.epfcore.epfcore.documentStudent.service;

import com.epfcore.epfcore.campus.entity.Campus;
import com.epfcore.epfcore.documentStudent.entity.DocumentStudent;
import com.epfcore.epfcore.documentStudent.repository.DocumentStudentRepository;
import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.student.entity.Student;
import com.epfcore.epfcore.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerationCertificateServiceTest {

    @Mock
    private DocumentStudentRepository documentRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private GenerationCertificateService generationCertificateService;

    private Student student;
    private User user;
    private Campus campus;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setLastname("Test");
        user.setFirstname("User");
        user.setBirthDate(LocalDate.now());

        campus = new Campus();
        campus.setVille("Testville");

        student = new Student();
        student.setId(1L);
        student.setUser(user);
        student.setAddress("Test address");
        student.setCampus(campus);
        student.setMajor("Test major");
        student.setEnrollmentDate(LocalDate.now());
    }

    @Test
    void generateFromRequest_existingDocument() {
        DocumentStudent doc = new DocumentStudent();
        doc.setFileUrl("dGVzdA=="); // "test" in base64
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(documentRepository.findByUserId(1)).thenReturn(Collections.singletonList(doc));

        byte[] result = generationCertificateService.generateFromRequest(1);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void generateFromRequest_newDocument() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(documentRepository.findByUserId(1)).thenReturn(Collections.emptyList());

        byte[] result = generationCertificateService.generateFromRequest(1);

        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(documentRepository, times(1)).save(any(DocumentStudent.class));
    }
}