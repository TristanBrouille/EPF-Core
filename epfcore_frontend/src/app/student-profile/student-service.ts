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

  certificate(studentId: number): Promise<Blob> {
  return firstValueFrom(
    this.http.get(`${this.baseUrl}/${studentId}/certificate`, {
      responseType: 'blob',
      withCredentials: true
    })
  );
}
}