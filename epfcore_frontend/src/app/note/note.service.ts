import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  CarnetDeNotes, Evaluation, Note, NoteInput,
  MoyenneRow, ImportResult
} from './note.model';

@Injectable({ providedIn: 'root' })
export class NoteService {

  private readonly API = 'http://localhost:8080/api';

  // Options communes à toutes les requêtes
  private readonly opts = { withCredentials: true };

  constructor(private http: HttpClient) {}

  getAllCarnets(): Observable<CarnetDeNotes[]> {
    return this.http.get<CarnetDeNotes[]>(`${this.API}/carnets`, this.opts);
  }

  getCarnet(id: number): Observable<CarnetDeNotes> {
    return this.http.get<CarnetDeNotes>(`${this.API}/carnets/${id}`, this.opts);
  }

  creerCarnet(data: {
    intitule: string;
    anneeAcademique: string;
    uniteEnseignementId?: number | null;
    moduleId?: number | null;
  }): Observable<CarnetDeNotes> {
    return this.http.post<CarnetDeNotes>(`${this.API}/carnets`, data, this.opts);
  }

  publierCarnet(id: number): Observable<CarnetDeNotes> {
    return this.http.patch<CarnetDeNotes>(`${this.API}/carnets/${id}/publier`, {}, this.opts);
  }

  getEvaluations(carnetId: number): Observable<Evaluation[]> {
    return this.http.get<Evaluation[]>(`${this.API}/carnets/${carnetId}/evaluations`, this.opts);
  }

  ajouterEvaluation(carnetId: number, data: {
    intitule: string;
    type: string;
    coef: number;
    noteMax: number;
  }): Observable<Evaluation> {
    return this.http.post<Evaluation>(`${this.API}/carnets/${carnetId}/evaluations`, data, this.opts);
  }

  saisirNote(input: NoteInput): Observable<Note> {
    return this.http.put<Note>(`${this.API}/notes/saisir`, input, this.opts);
  }

  importerCSV(evaluationId: number, file: File): Observable<ImportResult> {
    const form = new FormData();
    form.append('file', file, file.name);
    return this.http.post<ImportResult>(
      `${this.API}/notes/import/${evaluationId}`, form, this.opts
    );
  }

  getMoyennes(carnetId: number): Observable<MoyenneRow[]> {
    return this.http.get<MoyenneRow[]>(`${this.API}/carnets/${carnetId}/moyennes`, this.opts);
  }
}