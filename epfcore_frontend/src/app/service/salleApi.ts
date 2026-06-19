import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Salle } from '../model/salle';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class SalleApi {

  private readonly baseUrl = environment.apiUrl;

  private url(path: string): string {
    return `${this.baseUrl}/salle${path}`;
  }

  private readonly httpClient = inject(HttpClient);

    getSalles(): Observable<any[]> {
      const userString = localStorage.getItem('currentUser');
  console.log("Contenu brut de currentUser :", userString);
  const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
  const token = user.token;

  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });
  return this.httpClient.get<any[]>(this.url(''), { headers });
}

  addSalle(salle: Salle): Observable<Salle> {
    return this.httpClient.post<Salle>(this.url(''), salle);
  }

  deleteSalle(id: number): Observable<void> {
    return this.httpClient.delete<void>(this.url(`/${id}`));
  }

  getSalleById(id: number) {
    return this.httpClient.get<Salle>(this.url(`/${id}`));
  }

  updateSalle(salle: Salle) {
    return this.httpClient.put<Salle>(
      this.url(`/${salle.id}`),
      salle
    );
  }

getCampusOptions(): Observable<any[]> {
  return this.httpClient.get<any[]>(this.url(`/enums/campus`));
}
  getTypeSalleEnums(): Observable<string[]> {
    return this.httpClient.get<string[]>(this.url(`/enums/types`));
  }

}
