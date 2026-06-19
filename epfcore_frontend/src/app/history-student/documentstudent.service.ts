import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { DocumentStudent } from '../model/documentStudent';
import {environment} from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class DocumentStudentService {

  private readonly baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  private url(path: string): string {
    return `${this.baseUrl}/api/document_student${path}`;
  }

  getByUserId(userId: number): Promise<DocumentStudent[]> {
    return firstValueFrom(
      this.http.get<DocumentStudent[]>(
        this.url(`/user/${userId}`),
        { withCredentials: true }
      )
    );
  }

  etByStudentIdAndStatus(userId: number, status: string): Promise<DocumentStudent[]> {
    return firstValueFrom(
      this.http.get<DocumentStudent[]>(this.url(`/student/${userId}/status/${status}`), {
        withCredentials: true
      })
    );
  }

  create(document: Partial<DocumentStudent>): Promise<DocumentStudent> {
    return firstValueFrom(
      this.http.post<DocumentStudent>(this.url(``), document, {
        withCredentials: true
      })
    );
  }

  async certificate(studentId: number): Promise<Blob> {
    return firstValueFrom(
      this.http.get(this.url(`/generate/certificate/${studentId}`), {
        responseType: 'blob'
      })
    );
  }

    archiveDocument(id: number, archivedBy: string = 'system'): Promise<DocumentStudent> {
        return firstValueFrom(
            this.http.patch<DocumentStudent>(
              this.url(`/${id}/archive?archivedBy=${archivedBy}`),
                {}
            )
        );
    }

    getArchivedDocumentsByUser(userId: number): Promise<DocumentStudent[]> {
        return firstValueFrom(
            this.http.get<DocumentStudent[]>(
              this.url(`/user/${userId}/archived`),
                {}
            )
        );
    }

    unarchiveDocument(id: number): Promise<DocumentStudent> {
        return firstValueFrom(
            this.http.patch<DocumentStudent>(
              this.url(`/${id}/unarchive`),
                {},
                { withCredentials: true }
            )
        );
    }

    getAllArchivedDocuments(): Promise<DocumentStudent[]> {
        return firstValueFrom(
            this.http.get<DocumentStudent[]>(
              this.url(`/archived`),
                { withCredentials: true }
            )
        );
    }
  async downloadpdf(studentId: number): Promise<Blob> {
    return firstValueFrom(
      this.http.get(this.url(`/generate/pdf/${studentId}`), {
        responseType: 'blob'
      })
    );
  }
}
