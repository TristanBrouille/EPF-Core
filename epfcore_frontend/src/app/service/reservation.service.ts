import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ReservationApi {

  private readonly baseUrl = environment.apiUrl;
  private url(path: string): string {
    return `${this.baseUrl}/reservation${path}`;
  }

  private readonly httpClient = inject(HttpClient);

  addReservation(reservation: any): Observable<any> {
    return this.httpClient.post<any>(this.url(''), reservation);
  }

  getReservations(): Observable<any[]> {
    return this.httpClient.get<any[]>(this.url(''));
  }

  getReservationById(id: number): Observable<any> {
    return this.httpClient.get<any>(this.url(`/${id}`));
  }

  updateReservation(id: number, reservation: any): Observable<any> {
  return this.httpClient.post<any>(this.url(`/update/${id}`), reservation);
}

}
