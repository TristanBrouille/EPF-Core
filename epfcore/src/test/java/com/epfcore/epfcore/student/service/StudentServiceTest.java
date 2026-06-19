package com.epfcore.epfcore.student.service;

import com.epfcore.epfcore.student.entity.Student;
import com.epfcore.epfcore.student.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setStudentNumber("12345");
    }



    @Test
    void getStudentById_notFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> studentService.getStudentById(1L));
    }

    @Test
    void createStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        Student result = studentService.createStudent(new Student());
        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
    }

    @Test
    void updateStudent() {
        Student updatedStudent = new Student();
        updatedStudent.setStudentNumber("54321");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.updateStudent(1L, updatedStudent);

        assertNotNull(result);
        assertEquals("54321", result.getStudentNumber());
    }

    @Test
    void deleteStudent() {
        doNothing().when(studentRepository).deleteById(1L);
        studentService.deleteStudent(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }
}