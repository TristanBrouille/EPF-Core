import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Document } from '../model/document';

@Injectable({
    providedIn: 'root'
})
export class DocumentService {
    private baseUrl = 'http://localhost:8080/api/documents';

    constructor(private http: HttpClient) { }

    getByUserId(userId: number): Promise<Document[]> {
        return firstValueFrom(
            this.http.get<Document[]>(
                `${this.baseUrl}/user/${userId}`,
                { withCredentials: true }
            )
        );
    }

    etByStudentIdAndStatus(userId: number, status: string): Promise<Document[]> {
        return firstValueFrom(
            this.http.get<Document[]>(`${this.baseUrl}/student/${userId}/status/${status}`, {
                withCredentials: true
            })
        );
    }

    create(document: Partial<Document>): Promise<Document> {
        return firstValueFrom(
            this.http.post<Document>(`${this.baseUrl}`, document, {
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
}