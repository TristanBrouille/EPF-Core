import { firstValueFrom } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EntretienCandidature, Interviewer } from '../model/entretien';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class EntretienService {

  private readonly baseUrl = environment.apiUrl;

  constructor(private httpClient: HttpClient) {}

  private url(path: string): string {
    return `${this.baseUrl}/entretien${path}`;
  }

  getAll(): Promise<EntretienCandidature[]> {
    return firstValueFrom(
      this.httpClient.get<EntretienCandidature[]>(this.url(''))
    );
  }

  getMesEntretiens(): Promise<EntretienCandidature[]> {
    return firstValueFrom(
      this.httpClient.get<EntretienCandidature[]>(this.url('/mes-entretiens'))
    );
  }

  getById(id: number): Promise<EntretienCandidature> {
    return firstValueFrom(
      this.httpClient.get<EntretienCandidature>(this.url(`/${id}`))
    );
  }

  getByFormulaire(formulaireId: number): Promise<EntretienCandidature[]> {
    return firstValueFrom(
      this.httpClient.get<EntretienCandidature[]>(this.url(`/formulaire/${formulaireId}`))
    );
  }

  getInterviewers(): Promise<Interviewer[]> {
    return firstValueFrom(
      this.httpClient.get<Interviewer[]>(this.url('/interviewers'))
    );
  }

  create(entretien: EntretienCandidature): Promise<EntretienCandidature> {
    return firstValueFrom(
      this.httpClient.post<EntretienCandidature>(this.url(''), entretien)
    );
  }

  update(id: number, entretien: EntretienCandidature): Promise<EntretienCandidature> {
    return firstValueFrom(
      this.httpClient.put<EntretienCandidature>(this.url(`/${id}`), entretien)
    );
  }

  delete(id: number): Promise<void> {
    return firstValueFrom(
      this.httpClient.delete<void>(this.url(`/${id}`))
    );
  }
}
