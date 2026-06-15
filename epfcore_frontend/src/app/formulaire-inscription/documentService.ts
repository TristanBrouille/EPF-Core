import { firstValueFrom } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DocumentFormulaire } from '../model/document-formulaire';

@Injectable({
  providedIn: 'root',
})
export class DocumentService {

  private readonly baseUrl = 'http://localhost:8080/formulaire';

  constructor(private httpClient: HttpClient) {}

  getDocuments(formulaireId: number): Promise<DocumentFormulaire[]> {
    return firstValueFrom(
      this.httpClient.get<DocumentFormulaire[]>(`${this.baseUrl}/${formulaireId}/documents`)
    );
  }

  upload(formulaireId: number, documentType: string, file: File): Promise<DocumentFormulaire> {
    const formData = new FormData();
    formData.append('file', file);

    return firstValueFrom(
      this.httpClient.post<DocumentFormulaire>(
        `${this.baseUrl}/${formulaireId}/documents/${documentType}`,
        formData
      )
    );
  }

  download(formulaireId: number, documentType: string): Promise<Blob> {
    return firstValueFrom(
      this.httpClient.get(`${this.baseUrl}/${formulaireId}/documents/${documentType}`, {
        responseType: 'blob',
      })
    );
  }
}
