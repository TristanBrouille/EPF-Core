import { ChangeDetectorRef, Component, OnInit, signal, computed, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Student } from '../model/student';
import { StudentService } from '../student-profile/student-service';
import { ActivatedRoute, Router } from '@angular/router';
import { DocumentStudentService } from '../history-student/documentstudent.service';
import { DocumentStudent } from '../model/documentStudent';
import { DocumentStatus } from '../model/documentStudent';
import { ArchiveModalService } from '../app.service';

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
  hasArchivedDocs = computed(() => this.documents().some(d => d.status === 'ARCHIVED'));

  constructor(
    protected readonly router: Router,
    private route: ActivatedRoute,
    private studentService: StudentService,
    private documentService: DocumentStudentService,
    private cdr: ChangeDetectorRef,
    protected archiveModal: ArchiveModalService
  ) {
    effect(() => {
      const archivedId = this.archiveModal.onArchived();
      if (archivedId === null) return;

      this.documents.set(
        this.documents().map(d =>
          d.id === archivedId ? { ...d, status: DocumentStatus.ARCHIVED } : d
        )
      );
      this.archiveSuccess = archivedId;
      this.archiveModal.onArchived.set(null);
      setTimeout(() => { this.archiveSuccess = null; this.cdr.detectChanges(); }, 3000);
      this.cdr.detectChanges();
    });
    effect(() => {
      const unarchivedId = this.archiveModal.onUnarchived();
      if (unarchivedId === null) return;

      this.documents.update(list =>
        list.map(d => d.id === unarchivedId
          ? { ...d, status: DocumentStatus.APPROVED, archivedAt: undefined, archivedBy: undefined }
          : d)
      );
      this.archiveModal.onUnarchived.set(null);
      this.cdr.detectChanges();
    });
  }


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

  documentTypeLabel(type: string | undefined): string {
    if (!type) return '';
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

  dotClass(status: string): string {
    return {
      PENDING: 'dot-pending', IN_PROGRESS: 'dot-processing',
      APPROVED: 'dot-approved', REJECTED: 'dot-rejected', ARCHIVED: 'dot-archived'
    }[status] ?? '';
  }

  async archiveDocument(id: number): Promise<void> {
    if (!confirm('Archiver ce document ? Cette action est irréversible.')) return;
    try {
      await this.documentService.archiveDocument(id, 'admin');
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

  askArchive(doc: DocumentStudent): void {
    this.archiveModal.ask(doc, 'archive');
  }

  askUnarchive(doc: DocumentStudent): void {
    this.archiveModal.ask(doc, 'unarchive');
  }

}
