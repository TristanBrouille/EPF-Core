import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Student } from '../model/student';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private baseUrl = 'http://localhost:8080/api/students';
  private documentUrl = 'http://localhost:8080/api/document_student';

  constructor(private http: HttpClient) { }

  getAll(): Promise<Student[]> {
    return firstValueFrom(
      this.http.get<Student[]>(`${this.baseUrl}`, { withCredentials: true })
    );
  }

  getStudentById(id: number): Promise<Student> {
    return firstValueFrom(
      this.http.get<Student>(`${this.baseUrl}/${id}`, { withCredentials: true })
    );
  }

  getCurrentStudent(): Promise<Student> {
    return firstValueFrom(
      this.http.get<Student>(`${this.baseUrl}/me`)
    );
  }

  async certificate(studentId: number): Promise<Blob> {
    return firstValueFrom(
      this.http.get(`${this.documentUrl}/generate/certificate/${studentId}`, {
        responseType: 'blob'
      })
    );
  }

  async downloadpdf(studentId: number): Promise<Blob> {
    return firstValueFrom(
      this.http.get(`${this.documentUrl}/generate/pdf/${studentId}`, {
        responseType: 'blob'
      })
    );
  }

}