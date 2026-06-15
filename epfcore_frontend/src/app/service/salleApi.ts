import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Salle } from '../model/salle';

@Injectable({
  providedIn: 'root',
})
export class SalleApi {

  private readonly httpClient = inject(HttpClient);
  readonly url = "http://localhost:8080/salle"; // Votre URL d'origine

  getSalles(): Observable<Salle[]> {
    return this.httpClient.get<Salle[]>(this.url);
  }

  addSalle(salle: Salle): Observable<Salle> {
    return this.httpClient.post<Salle>(this.url, salle);
  }

  deleteSalle(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.url}/${id}`);
  }

  getSalleById(id: number) {
    return this.httpClient.get<Salle>(`${this.url}/${id}`);
  }

  updateSalle(salle: Salle) {
    return this.httpClient.put<Salle>(
      `${this.url}/${salle.id}`,
      salle
    );
  }

getCampusOptions(): Observable<any[]> {
  return this.httpClient.get<any[]>(`${this.url}/enums/campus`);
}
  getTypeSalleEnums(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${this.url}/enums/types`);
  }

}