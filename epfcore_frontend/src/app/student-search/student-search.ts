import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Student } from '../model/student';
import { StudentService } from '../student-profile/student-service';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-student-search',
  imports: [CommonModule],
  templateUrl: './student-search.html',
  styleUrl: './student-search.scss',
})
export class StudentSearch implements OnInit {

  student: Student | null = null;
  
    constructor(
      protected readonly router: Router,
      private route: ActivatedRoute,
      private studentService: StudentService,
      private cdr: ChangeDetectorRef
    ) { }


      ngOnInit(): void {
    const studentId = Number(this.route.snapshot.paramMap.get('id'));
    if (!studentId) return;
 
    this.studentService.getStudentById(studentId).then(student => {
      this.student = student;
      this.cdr.detectChanges();
    }).catch(err => {
      console.error('Erreur lors du chargement de l\'étudiant', err);
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

  async certificate(): Promise<void> {
    if (!this.student) return;

    try {
      const pdfBlob = await this.studentService.certificate(this.student.id);

      const url = window.URL.createObjectURL(pdfBlob);

      const a = document.createElement('a');
      a.href = url;
      a.download = `certificat_scolarite_${this.student.user.firstname}_${this.student.user.lastname}.pdf`;
      a.click();

      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Erreur génération certificat PDF', err);
    }
  }

  async downloadpdf(): Promise<void> {
    if (!this.student) return;

    try {
      const pdfBlob = await this.studentService.downloadpdf(this.student.id);

      const url = window.URL.createObjectURL(pdfBlob);

      const a = document.createElement('a');
      a.href = url;
      a.download = `Infos_personnelles_${this.student.user.firstname}_${this.student.user.lastname}.pdf`;
      a.click();

      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Erreur génération PDF', err);
    }
  }

}
