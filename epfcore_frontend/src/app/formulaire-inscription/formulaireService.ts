import { firstValueFrom } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Formulaire } from '../model/formulaire';

@Injectable({
  providedIn: 'root',
})
export class FormulaireService {

  private readonly baseUrl = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) {}

  private url(path: string): string {
    return `${this.baseUrl}/${path}`;
  }

  getMyFormulaire(): Promise<Formulaire> {
    return firstValueFrom(
      this.httpClient.get<Formulaire>(this.url('formulaire/me'))
    );
  }

  create(formulaire: Formulaire): Promise<Formulaire> {
    return firstValueFrom(
      this.httpClient.post<Formulaire>(this.url('formulaire'), formulaire)
    );
  }

  update(formulaire: Formulaire): Promise<Formulaire> {
    return firstValueFrom(
      this.httpClient.put<Formulaire>(this.url('formulaire'), formulaire)
    );
  }

  delete(id: number): Promise<void> {
    return firstValueFrom(
      this.httpClient.delete<void>(this.url(`formulaire/${id}`))
    );
  }
}
