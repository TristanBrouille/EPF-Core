import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Student } from '../model/student';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private baseUrl = 'http://localhost:8080/api/students';

  constructor(private http: HttpClient) { }

  getCurrentStudent(): Promise<Student> {
    return firstValueFrom(
      this.http.get<Student>(`${this.baseUrl}/me`)
    );
  }

  downloadPdf(studentId: number): Promise<Blob> {
    return firstValueFrom(
      this.http.get(`${this.baseUrl}/${studentId}/pdf`, {
        responseType: 'blob',
        withCredentials: true
      })
    );
  }

  //   certificate(studentId: number): Promise<Blob> {
  //   return firstValueFrom(
  //     this.http.get(`${this.baseUrl}/${studentId}/certificate`, {
  //       responseType: 'blob',
  //       withCredentials: true
  //     })
  //   );
  // }

  async certificate(studentId: number): Promise<Blob> {
    const request = await firstValueFrom(
      this.http.post<{ id: number }>('http://localhost:8080/api/document-requests', {
        student: { id: studentId },
        documentType: 'CERTIFICATE'
      })
    );
    return firstValueFrom(
      this.http.get(`http://localhost:8080/api/document-requests/${request.id}/generate/certificate`, {
        responseType: 'blob'
      })
    );
  }
}