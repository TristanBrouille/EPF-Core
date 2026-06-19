import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Student } from '../model/student';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private readonly baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  private url(path: string): string {
    return `${this.baseUrl}/api/${path}`;
  }

  getAll(): Promise<Student[]> {
    return firstValueFrom(
      this.http.get<Student[]>(this.url(`students`), { withCredentials: true })
    );
  }

  getStudentById(id: number): Promise<Student> {
    return firstValueFrom(
      this.http.get<Student>(this.url(`students/${id}`), { withCredentials: true })
    );
  }

  getCurrentStudent(): Promise<Student> {
    return firstValueFrom(
      this.http.get<Student>(this.url(`students/me`))
    );
  }

  async certificate(studentId: number): Promise<Blob> {
    return firstValueFrom(
      this.http.get(this.url(`document_student/generate/certificate/${studentId}`), {
        responseType: 'blob'
      })
    );
  }

  async downloadpdf(studentId: number): Promise<Blob> {
    return firstValueFrom(
      this.http.get(this.url(`document_student/generate/pdf/${studentId}`), {
        responseType: 'blob'
      })
    );
  }

}
