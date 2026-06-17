package com.epfcore.epfcore.student.dto;

import java.time.LocalDate;

import com.epfcore.epfcore.student.entity.Gender;
import com.epfcore.epfcore.student.entity.Student;

public class StudentDTO {

    private Long id;
    private String studentNumber;
    private UserDocDTO user;   
    private Gender gender;
    private String nationality;
    private String phone;
    private String address;
    private String academicYear;
    private String major;  
    private String program;
    private String campus;
    private Boolean scholarship;
    private String lastDegree;
    private String photoUrl;
    private LocalDate enrollmentDate;

    public StudentDTO() {}

    public StudentDTO(Student student) {
        this.id = student.getId();
        this.studentNumber = student.getStudentNumber();
        this.user = new UserDocDTO(student.getUser()); 
        this.gender = student.getGender();
        this.nationality = student.getNationality();
        this.phone = student.getPhone();
        this.address = student.getAddress();
        this.academicYear = student.getAcademicYear();
        this.major = student.getMajor();
        this.program = student.getProgram();
        this.campus = student.getCampus().getVille();
        this.scholarship = student.getScholarship();
        this.lastDegree = student.getLastDegree();
        this.photoUrl = student.getPhotoUrl();
        this.enrollmentDate = student.getEnrollmentDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public UserDocDTO getUser() {
        return user;
    }

    public void setUser(UserDocDTO user) {
        this.user = user;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public Boolean getScholarship() {
        return scholarship;
    }

    public void setScholarship(Boolean scholarship) {
        this.scholarship = scholarship;
    }

    public String getLastDegree() {
        return lastDegree;
    }

    public void setLastDegree(String lastDegree) {
        this.lastDegree = lastDegree;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
}
