import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';

import { Student } from '../model/student';



@Component({
  selector: 'app-student-profile',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './student-profile.html',
  styleUrls: ['./student-profile.scss'],
})
export class StudentProfile implements OnInit {

  student: Student | null = null;

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadStudentConnecte();
  }


  loadStudentConnecte(): void {
    this.http.get<Student>(
      'http://localhost:8080/api/students/me',
      { withCredentials: true }
    ).subscribe({
      next: (data) => {
        this.student = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur chargement student connecté', err);
      }
    });
  }

  get initialesAvatar(): string {
    if (!this.student) return '';
    return `${this.student.user.firstname[0]}${this.student.user.lastname[0]}`.toUpperCase();
  }

  get dateInscriptionFormatee(): string {
    if (!this.student) return '';
    return new Date(this.student.enrollmentDate).toLocaleDateString('fr-FR', {
      day: '2-digit', month: 'long', year: 'numeric'
    });
  }

  get telephoneFormate(): string {
    if (!this.student) return '';
    return this.student.phone.replace(/(\d{2})(?=\d)/g, '$1 ');
  }

  downloadPDF(): void {
    if (!this.student) return;

    this.http.get(
      `http://localhost:8080/api/students/${this.student.id}/pdf`,
      {
        responseType: 'blob',
        withCredentials: true
      }
    ).subscribe({
      next: (pdfBlob: Blob) => {

        const url = window.URL.createObjectURL(pdfBlob);

        const a = document.createElement('a');
        a.href = url;
        a.download = `student-${this.student?.studentNumber}.pdf`;
        a.click();

        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Erreur génération PDF', err);
      }
    });
  }



}
