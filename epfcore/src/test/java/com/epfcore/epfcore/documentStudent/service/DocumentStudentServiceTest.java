package com.epfcore.epfcore.documentStudent.service;

import com.epfcore.epfcore.documentStudent.entity.DocumentStatus;
import com.epfcore.epfcore.documentStudent.entity.DocumentStudent;
import com.epfcore.epfcore.documentStudent.repository.DocumentStudentRepository;
import com.epfcore.epfcore.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentStudentServiceTest {

    @Mock
    private DocumentStudentRepository documentRepository;

    @InjectMocks
    private DocumentStudentService documentStudentService;

    private DocumentStudent document;

    @BeforeEach
    void setUp() {
        document = new DocumentStudent();
        document.setId(1);
        document.setStatus(DocumentStatus.PENDING);
    }

    @Test
    void getById() {
        when(documentRepository.findById(1)).thenReturn(Optional.of(document));
        DocumentStudent result = documentStudentService.getById(1);
        assertNotNull(result);
        assertEquals(document.getId(), result.getId());
    }

    @Test
    void create() {
        when(documentRepository.save(any(DocumentStudent.class))).thenReturn(document);
        DocumentStudent result = documentStudentService.create(new DocumentStudent());
        assertNotNull(result);
        assertNotNull(result.getCreationDate());
    }

    @Test
    void update_archivedDocument_shouldThrowException() {
        document.setStatus(DocumentStatus.ARCHIVED);
        when(documentRepository.findById(1)).thenReturn(Optional.of(document));
        assertThrows(ResponseStatusException.class, () -> documentStudentService.update(1, new DocumentStudent()));
    }

    @Test
    void delete_archivedDocument_shouldThrowException() {
        document.setStatus(DocumentStatus.ARCHIVED);
        when(documentRepository.findById(1)).thenReturn(Optional.of(document));
        assertThrows(ResponseStatusException.class, () -> documentStudentService.delete(1));
    }

    @Test
    void archive() {
        when(documentRepository.findById(1)).thenReturn(Optional.of(document));
        when(documentRepository.save(any(DocumentStudent.class))).thenReturn(document);

        DocumentStudent result = documentStudentService.archive(1, "admin");

        assertEquals(DocumentStatus.ARCHIVED, result.getStatus());
        assertNotNull(result.getArchivedAt());
        assertEquals("admin", result.getArchivedBy());
    }

    @Test
    void unarchive() {
        document.setStatus(DocumentStatus.ARCHIVED);
        when(documentRepository.findById(1)).thenReturn(Optional.of(document));
        when(documentRepository.save(any(DocumentStudent.class))).thenReturn(document);

        DocumentStudent result = documentStudentService.unarchive(1);

        assertEquals(DocumentStatus.APPROVED, result.getStatus());
        assertNull(result.getArchivedAt());
        assertNull(result.getArchivedBy());
    }
}