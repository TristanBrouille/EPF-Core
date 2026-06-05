import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { DocumentRequest } from '../model/documentrequest';

@Injectable({
    providedIn: 'root'
})
export class DocumentRequestService {
    private baseUrl = 'http://localhost:8080/api/document-requests';

    constructor(private http: HttpClient) { }

    getByStudentId(studentId: number): Promise<DocumentRequest[]> {
        return firstValueFrom(
            this.http.get<DocumentRequest[]>(
                `${this.baseUrl}/student/${studentId}`,
                { withCredentials: true }
            )
        );
    }

    etByStudentIdAndStatus(studentId: number, status: string): Promise<DocumentRequest[]> {
        return firstValueFrom(
            this.http.get<DocumentRequest[]>(`${this.baseUrl}/student/${studentId}/status/${status}`, {
                withCredentials: true
            })
        );
    }

    create(request: Partial<DocumentRequest>): Promise<DocumentRequest> {
        return firstValueFrom(
            this.http.post<DocumentRequest>(`${this.baseUrl}`, request, {
                withCredentials: true
            })
        );
    }

    async certificate(studentId: number): Promise<Blob> {
        return firstValueFrom(
            this.http.get(`http://localhost:8080/api/document-requests/generate/certificate/${studentId}`, {
                responseType: 'blob'
            })
        );
    }

    async downloadpdf(studentId: number): Promise<Blob> {
        return firstValueFrom(
            this.http.get(`http://localhost:8080/api/document-requests/generate/pdf/${studentId}`, {
                responseType: 'blob'
            })
        );
    }

}