import { firstValueFrom } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DocumentFormulaire } from '../model/document-formulaire';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DocumentService {

  private readonly baseUrl = environment.apiUrl;
  private url(path: string): string {
    return `${this.baseUrl}/formulaire/${path}`;
  }


  constructor(private httpClient: HttpClient) {}

  getDocuments(formulaireId: number): Promise<DocumentFormulaire[]> {
    return firstValueFrom(
      this.httpClient.get<DocumentFormulaire[]>(this.url(`${formulaireId}/documents`))
    );
  }

  upload(formulaireId: number, documentType: string, file: File): Promise<DocumentFormulaire> {
    const formData = new FormData();
    formData.append('file', file);

    return firstValueFrom(
      this.httpClient.post<DocumentFormulaire>(
        this.url(`${formulaireId}/documents/${documentType}`),
        formData
      )
    );
  }

  download(formulaireId: number, documentType: string): Promise<Blob> {
    return firstValueFrom(
      this.httpClient.get(this.url(`${formulaireId}/documents/${documentType}`), {
        responseType: 'blob',
      })
    );
  }
}
