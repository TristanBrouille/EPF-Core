import { ChangeDetectorRef, Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Student } from '../model/student';
import { StudentService } from '../student-profile/student-service';
import { ActivatedRoute, Router } from '@angular/router';
import { DocumentStudentService } from '../history-student/documentstudent.service';
import { DocumentStudent } from '../model/documentStudent';
import { DocumentStatus } from '../model/documentStudent';

@Component({
  selector: 'app-student-search',
  imports: [CommonModule],
  templateUrl: './student-search.html',
  styleUrl: './student-search.scss',
})

export class StudentSearch implements OnInit {

  student: Student | null = null;
  documents = signal<DocumentStudent[]>([]);
  archiveSuccess: number | null = null;

  constructor(
    protected readonly router: Router,
    private route: ActivatedRoute,
    private studentService: StudentService,
    private documentService: DocumentStudentService,
    private cdr: ChangeDetectorRef
  ) { }


  ngOnInit(): void {
    const studentId = Number(this.route.snapshot.paramMap.get('id'));
    if (!studentId) return;

    this.studentService.getStudentById(studentId).then(student => {
      this.student = student;
      this.cdr.detectChanges();
      return this.documentService.getByUserId(student.user.id);
    }).then(docs => {
      this.documents.set(docs);
      this.cdr.detectChanges();
    }).catch(err => {
      console.error('Erreur chargement', err);
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

  documentTypeLabel(type: string): string {
    return ({
      CERTIFICATE_SCOLAR: 'Certificat de scolarité',
      INFOS_STUDENT: 'Informations personnelles',
    } as Record<string, string>)[type] ?? type;
  }

  docIconClass(type: string): string {
    return { CERTIFICATE_SCOLAR: 'doc-cert', INFOS_STUDENT: 'doc-infos' }[type] ?? '';
  }

  docIconTi(type: string): string {
    return ({
      CERTIFICATE_SCOLAR: 'ti-certificate',
      INFOS_STUDENT: 'ti-user'
    } as Record<string, string>)[type] ?? 'ti-file';
  }

  async archiveDocument(id: number): Promise<void> {
    if (!confirm('Archiver ce document ? Cette action est irréversible.')) return;
    try {
      await this.documentService.archiveDocument(id, 'admin');
      // Mettre à jour localement sans recharger
      this.documents.set(
        this.documents().map(d =>
          d.id === id ? { ...d, status: DocumentStatus.ARCHIVED } : d
        )
      );
      this.archiveSuccess = id;
      setTimeout(() => { this.archiveSuccess = null; this.cdr.detectChanges(); }, 3000);
      this.cdr.detectChanges();
    } catch (err) {
      console.error('Erreur archivage', err);
    }
  }

  async unarchiveDocument(id: number): Promise<void> {
  try {
    await this.documentService.unarchiveDocument(id);
    this.documents.update(list =>
      list.map(d => d.id === id ? { ...d, status: DocumentStatus.APPROVED, archivedAt: undefined, archivedBy: undefined } : d)
    );
  } catch (err) {
    console.error('Erreur désarchivage', err);
  }
}



}
