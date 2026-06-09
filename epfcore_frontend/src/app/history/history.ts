import { Component, OnInit, Inject, signal } from '@angular/core';
import { Document } from '../model/document';
import { DocumentService } from './document.service';
import { StudentService } from '../student-profile/student-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {Student } from '../model/student';

@Component({
  selector: 'app-history',
  imports: [CommonModule, FormsModule],
  templateUrl: './history.html',
  styleUrl: './history.scss',
})
export class History implements OnInit {

  searchTerm = '';
  filterStatus = '';
  filterType = '';
  student: Student | null = null;

  constructor(
  @Inject(DocumentService) private documentService: DocumentService,
  @Inject(StudentService) private studentService: StudentService
) { }

  requests = signal<Document[]>([]);

  ngOnInit(): void {
    this.studentService.getCurrentStudent().then(student => {
      this.student = student;
      this.documentService.getByUserId(student.user.id).then(data => {
        this.requests.set(data);
      });
    });
  }

  filteredRequests(): Document[] {
    return this.requests().filter(r => {
      const matchSearch = !this.searchTerm ||
        r.documentType.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchStatus = !this.filterStatus || r.status === this.filterStatus;
      const matchType = !this.filterType || r.documentType === this.filterType;
      return matchSearch && matchStatus && matchType;
    });
  }

  countFiltered(status: string): number {
    return this.filteredRequests().filter(r => r.status === status).length;
  }

  badgeClass(status: string): string {
    return {
      PENDING: 'badge-pending', IN_PROGRESS: 'badge-processing',
      APPROVED: 'badge-approved', REJECTED: 'badge-rejected'
    }[status] ?? '';
  }

  dotClass(status: string): string {
    return {
      PENDING: 'dot-pending', IN_PROGRESS: 'dot-processing',
      APPROVED: 'dot-approved', REJECTED: 'dot-rejected'
    }[status] ?? '';
  }

  docIconClass(type: string): string {
    return { CERTIFICATE: 'doc-cert', INFOS: 'doc-infos', }[type] ?? '';
  }

  docIconTi(type: string): string {
    return ({
      CERTIFICATE: 'ti-certificate',
      INFOS: 'ti-user'
    } as Record<string, string>)[type] ?? 'ti-file';
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
      CERTIFICATE: 'Certificat de scolarité',
      INFOS: 'Informations personnelles',
    } as Record<string, string>)[type] ?? type;
  }
}
