import { Component, OnInit, Inject, signal } from '@angular/core';
import { DocumentStudent } from '../model/documentStudent';
import { DocumentStudentService } from '../history-student/documentstudent.service';
import { StudentService } from '../student-profile/student-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Student } from '../model/student';

interface ArchiveGroup {
  userId: number;
  studentId: number;
  firstname: string;
  lastname: string;
  docs: DocumentStudent[];
}

@Component({
  selector: 'app-archive-documents',
  imports: [CommonModule, FormsModule],
  templateUrl: './archive-documents.html',
  styleUrl: './archive-documents.scss',
})
export class ArchiveDocuments implements OnInit {

  searchTerm = '';
  filterType = '';
  student: Student | null = null;

  archives = signal<DocumentStudent[]>([]);
  hoveredUserId = signal<number | null>(null);

  constructor(
    @Inject(DocumentStudentService) private documentService: DocumentStudentService,
    @Inject(StudentService) private studentService: StudentService
  ) { }

  ngOnInit(): void {
    this.documentService.getAllArchivedDocuments().then(data => {
      this.archives.set(data);
    });
  }

  filteredArchives(): DocumentStudent[] {
    return this.archives().filter(r => {
      const matchSearch = !this.searchTerm ||
        r.documentType.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        r.firstname.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        r.lastname.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchType = !this.filterType || r.documentType === this.filterType;
      return matchSearch && matchType;
    });
  }

  groupedArchives(): ArchiveGroup[] {
    const groups = new Map<number, ArchiveGroup>();

    for (const doc of this.filteredArchives()) {
      if (!groups.has(doc.userId)) {
        groups.set(doc.userId, {
          userId: doc.userId,
          studentId: doc.studentId,
          firstname: doc.firstname,
          lastname: doc.lastname,
          docs: []
        });
      }
      groups.get(doc.userId)!.docs.push(doc);
    }

    return Array.from(groups.values());
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

  documentTypeLabel(type: string): string {
    return ({
      CERTIFICATE_SCOLAR: 'Certificat de scolarité',
      INFOS_STUDENT: 'Informations personnelles',
    } as Record<string, string>)[type] ?? type;
  }

  async downloadArchive(doc: DocumentStudent): Promise<void> {
    try {
      let pdfBlob: Blob;
      let filename: string;
      if (doc.documentType.trim() === 'CERTIFICATE_SCOLAR') {
        pdfBlob = await this.studentService.certificate(doc.studentId);
        filename = `certificat_scolarite_${doc.firstname}_${doc.lastname}.pdf`;
      } else {
        pdfBlob = await this.studentService.downloadpdf(doc.studentId);
        filename = `infos_personnelles_${doc.firstname}_${doc.lastname}.pdf`;
      }
      const url = window.URL.createObjectURL(pdfBlob);
      const a = document.createElement('a');
      a.href = url;
      a.download = filename;
      a.click();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Erreur téléchargement archive', err);
    }
  }

}
