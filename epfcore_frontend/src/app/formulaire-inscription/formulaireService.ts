import { firstValueFrom } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DecisionAdmission, Formulaire } from '../model/formulaire';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class FormulaireService {

  private readonly baseUrl = environment.apiUrl;

  constructor(private httpClient: HttpClient) {}

  private url(path: string): string {
    return `${this.baseUrl}/formulaire${path}`;
  }

  getMyFormulaire(): Promise<Formulaire> {
    return firstValueFrom(
      this.httpClient.get<Formulaire>(this.url('/candidatform'))
    );
  }

  getAllSoumis(): Promise<Formulaire[]> {
    return firstValueFrom(
      this.httpClient.get<Formulaire[]>(this.url('/soumis'))
    );
  }

  getById(id: number): Promise<Formulaire> {
    return firstValueFrom(
      this.httpClient.get<Formulaire>(this.url(`/${id}`))
    );
  }

  create(formulaire: Formulaire): Promise<Formulaire> {
    return firstValueFrom(
      this.httpClient.post<Formulaire>(this.url(''), formulaire)
    );
  }

  update(formulaire: Formulaire): Promise<Formulaire> {
    return firstValueFrom(
      this.httpClient.put<Formulaire>(this.url(''), formulaire)
    );
  }

  updateDecision(id: number, decision: DecisionAdmission): Promise<Formulaire> {
    return firstValueFrom(
      this.httpClient.patch<Formulaire>(this.url(`/${id}/decision`), null, { params: { decision } })
    );
  }

  delete(id: number): Promise<void> {
    return firstValueFrom(
      this.httpClient.delete<void>(this.url(`/${id}`))
    );
  }
}
