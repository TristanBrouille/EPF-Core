import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { DocumentStudent } from '../model/documentStudent';

@Injectable({
    providedIn: 'root'
})
export class DocumentStudentService {
    private baseUrl = 'http://localhost:8080/api/document_student';

    constructor(private http: HttpClient) { }

    getByUserId(userId: number): Promise<DocumentStudent[]> {
        return firstValueFrom(
            this.http.get<DocumentStudent[]>(
                `${this.baseUrl}/user/${userId}`,
                { withCredentials: true }
            )
        );
    }

    etByStudentIdAndStatus(userId: number, status: string): Promise<DocumentStudent[]> {
        return firstValueFrom(
            this.http.get<DocumentStudent[]>(`${this.baseUrl}/student/${userId}/status/${status}`, {
                withCredentials: true
            })
        );
    }

    create(document: Partial<DocumentStudent>): Promise<DocumentStudent> {
        return firstValueFrom(
            this.http.post<DocumentStudent>(`${this.baseUrl}`, document, {
                withCredentials: true
            })
        );
    }

    async certificate(studentId: number): Promise<Blob> {
        return firstValueFrom(
            this.http.get(`${this.baseUrl}/generate/certificate/${studentId}`, {
                responseType: 'blob'
            })
        );
    }

    async downloadpdf(studentId: number): Promise<Blob> {
        return firstValueFrom(
            this.http.get(`${this.baseUrl}/generate/pdf/${studentId}`, {
                responseType: 'blob'
            })
        );
    }

    archiveDocument(id: number, archivedBy: string = 'system'): Promise<DocumentStudent> {
        return firstValueFrom(
            this.http.patch<DocumentStudent>(
                `${this.baseUrl}/${id}/archive?archivedBy=${archivedBy}`,
                {}
            )
        );
    }

    getArchivedDocumentsByUser(userId: number): Promise<DocumentStudent[]> {
        return firstValueFrom(
            this.http.get<DocumentStudent[]>(
                `${this.baseUrl}/user/${userId}/archived`,
                {}
            )
        );
    }

    unarchiveDocument(id: number): Promise<DocumentStudent> {
        return firstValueFrom(
            this.http.patch<DocumentStudent>(
                `${this.baseUrl}/${id}/unarchive`,
                {},
                { withCredentials: true }
            )
        );
    }

    getAllArchivedDocuments(): Promise<DocumentStudent[]> {
        return firstValueFrom(
            this.http.get<DocumentStudent[]>(
                `${this.baseUrl}/archived`,
                { withCredentials: true }
            )
        );
    }
}