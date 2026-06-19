import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  CarnetDeNotes, Evaluation, Note, NoteInput,
  MoyenneRow, ImportResult
} from './note.model';
import {environment} from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class NoteService {

  private readonly baseUrl = environment.apiUrl;
  private url(path: string): string {
    return `${this.baseUrl}/api${path}`;
  }


  constructor(private http: HttpClient) {}


  getAllCarnets(): Observable<CarnetDeNotes[]> {
    return this.http.get<CarnetDeNotes[]>(this.url(`/carnets`));
  }

  getCarnet(id: number): Observable<CarnetDeNotes> {
    return this.http.get<CarnetDeNotes>(this.url(`/carnets/${id}`));
  }

  creerCarnet(data: {
    intitule: string;
    anneeAcademique: string;
    uniteEnseignementId?: number | null;
    moduleId?: number | null;
  }): Observable<CarnetDeNotes> {
    return this.http.post<CarnetDeNotes>(this.url(`/carnets`), data);
  }

  publierCarnet(id: number): Observable<CarnetDeNotes> {
    return this.http.patch<CarnetDeNotes>(this.url(`/carnets/${id}/publier`), {});
  }

  getEvaluations(carnetId: number): Observable<Evaluation[]> {
    return this.http.get<Evaluation[]>(this.url(`/carnets/${carnetId}/evaluations`));
  }

  ajouterEvaluation(carnetId: number, data: {
    intitule: string;
    type: string;
    coef: number;
    noteMax: number;
  }): Observable<Evaluation> {
    return this.http.post<Evaluation>(this.url(`/carnets/${carnetId}/evaluations`), data);
  }

  importerCSV(evaluationId: number, file: File): Observable<ImportResult> {
    const form = new FormData();
    form.append('file', file, file.name);
    return this.http.post<ImportResult>(
      this.url(`/notes/import/${evaluationId}`), form
    );
  }

  getMoyennes(carnetId: number): Observable<MoyenneRow[]> {
    return this.http.get<MoyenneRow[]>(this.url(`/carnets/${carnetId}/moyennes`));
  }

  getNotesByCarnet(carnetId: number): Observable<any[]> {
    return this.http.get<any[]>(this.url(`/${carnetId}/notes`));
  }

  saisirNote(data: { evaluationId: number; etudiantId: number; valeur: number | null; commentaire: string }): Observable<any> {
    return this.http.patch<any>(this.url(`/notes/saisir`), data);
  }
}
