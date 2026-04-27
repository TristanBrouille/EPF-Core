import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Student } from '../model/student';
import { StudentService } from '../services/student-service';

@Component({
  selector: 'app-student-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './student-profile.html',
  styleUrls: ['./student-profile.scss'],
})
export class StudentProfile implements OnInit {

  student: Student | null = null;

  constructor(
    private studentService: StudentService,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    await this.loadStudentConnecte();
  }

  async loadStudentConnecte(): Promise<void> {
    try {
      this.student = await this.studentService.getCurrentStudent();
      this.cdr.detectChanges();
    } catch (err) {
      console.error('Erreur chargement student connecté', err);
    }
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

  async downloadPDF(): Promise<void> {
    if (!this.student) return;

    try {
      const pdfBlob = await this.studentService.downloadPdf(this.student.id);

      const url = window.URL.createObjectURL(pdfBlob);

      const a = document.createElement('a');
      a.href = url;
      a.download = `student-${this.student.studentNumber}.pdf`;
      a.click();

      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Erreur génération PDF', err);
    }
  }
}