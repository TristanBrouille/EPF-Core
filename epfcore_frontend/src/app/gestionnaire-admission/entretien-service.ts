import { firstValueFrom } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EntretienCandidature, Interviewer } from '../model/entretien';

@Injectable({
  providedIn: 'root',
})
export class EntretienService {

  private readonly baseUrl = 'http://localhost:8080/entretien';

  constructor(private httpClient: HttpClient) {}

  private url(path: string): string {
    return `${this.baseUrl}/${path}`;
  }

  getAll(): Promise<EntretienCandidature[]> {
    return firstValueFrom(
      this.httpClient.get<EntretienCandidature[]>(this.baseUrl)
    );
  }

  getMesEntretiens(): Promise<EntretienCandidature[]> {
    return firstValueFrom(
      this.httpClient.get<EntretienCandidature[]>(this.url('mes-entretiens'))
    );
  }

  getById(id: number): Promise<EntretienCandidature> {
    return firstValueFrom(
      this.httpClient.get<EntretienCandidature>(this.url(`${id}`))
    );
  }

  getByFormulaire(formulaireId: number): Promise<EntretienCandidature[]> {
    return firstValueFrom(
      this.httpClient.get<EntretienCandidature[]>(this.url(`formulaire/${formulaireId}`))
    );
  }

  getInterviewers(): Promise<Interviewer[]> {
    return firstValueFrom(
      this.httpClient.get<Interviewer[]>(this.url('interviewers'))
    );
  }

  create(entretien: EntretienCandidature): Promise<EntretienCandidature> {
    return firstValueFrom(
      this.httpClient.post<EntretienCandidature>(this.baseUrl, entretien)
    );
  }

  update(id: number, entretien: EntretienCandidature): Promise<EntretienCandidature> {
    return firstValueFrom(
      this.httpClient.put<EntretienCandidature>(this.url(`${id}`), entretien)
    );
  }

  delete(id: number): Promise<void> {
    return firstValueFrom(
      this.httpClient.delete<void>(this.url(`${id}`))
    );
  }
}
