package com.epfcore.epfcore.student.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.epfcore.epfcore.student.dto.StudentDTO;
import com.epfcore.epfcore.student.entity.Student;
import com.epfcore.epfcore.student.repository.StudentRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentDTO::new)
                .toList();
    }

    public StudentDTO getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(StudentDTO::new)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student non trouvé avec Id : " + id));
    }

    public StudentDTO getStudentByNumStudent(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber)
                .map(StudentDTO::new)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student non trouvé avec studentNumber : " + studentNumber));
    }

    public StudentDTO getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .map(StudentDTO::new)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student non trouvé avec userId : " + userId));
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        return studentRepository.findById(id)
                .map(student -> {

                    student.setStudentNumber(updatedStudent.getStudentNumber());
                    student.setUser(updatedStudent.getUser());
                    student.setGender(updatedStudent.getGender());
                    student.setNationality(updatedStudent.getNationality());
                    student.setPhone(updatedStudent.getPhone());
                    student.setAddress(updatedStudent.getAddress());
                    student.setAcademicYear(updatedStudent.getAcademicYear());
                    student.setMajor(updatedStudent.getMajor());
                    student.setProgram(updatedStudent.getProgram());
                    student.setCampus(updatedStudent.getCampus());
                    student.setScholarship(updatedStudent.getScholarship());
                    student.setLastDegree(updatedStudent.getLastDegree());
                    student.setPhotoUrl(updatedStudent.getPhotoUrl());
                    student.setEnrollmentDate(updatedStudent.getEnrollmentDate());

                    return studentRepository.save(student);
                })
                .orElseThrow(() -> new RuntimeException("Student non trouvé avec l'id : " + id));
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public StudentDTO findByUserEmail(String email) {
        return studentRepository.findByUserEmail(email)
                .map(StudentDTO::new)
                .orElseThrow(() -> new RuntimeException(
                        "Student non trouvé pour email: " + email));
    }

}
