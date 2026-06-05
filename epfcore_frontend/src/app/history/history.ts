import { Component, OnInit } from '@angular/core';
import { DocumentRequest } from '../model/documentrequest';
import { DocumentRequestService } from '../history/document-request.service';
import { StudentService } from '../student-profile/student-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-history',
  imports: [CommonModule, FormsModule],
  templateUrl: './history.html',
  styleUrl: './history.scss',
})
export class History implements OnInit {

  requests: DocumentRequest[] = [];
  searchTerm = '';
  filterStatus = '';
  filterType = '';

  constructor(private documentService: DocumentRequestService, private studentService: StudentService ) { }

  ngOnInit(): void {
    this.studentService.getCurrentStudent().then(student => {
      this.documentService.getByStudentId(student.id).then(data => {
        this.requests = data;
      });
    });
  }

  filteredRequests(): DocumentRequest[] {
    return this.requests.filter(r => {
      const matchSearch = !this.searchTerm ||
        r.documentType.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchStatus = !this.filterStatus || r.status === this.filterStatus;
      const matchType = !this.filterType || r.documentType === this.filterType;
      return matchSearch && matchStatus && matchType;
    });
  }

  countByStatus(status: string): number {
    return this.requests.filter(r => r.status === status).length;
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




}
