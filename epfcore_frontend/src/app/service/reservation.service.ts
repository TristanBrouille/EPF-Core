import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReservationApi {

  private readonly httpClient = inject(HttpClient);
  readonly url = "http://localhost:8080/reservation"; 

  addReservation(reservation: any): Observable<any> {
    return this.httpClient.post<any>(this.url, reservation);
  }

  getReservations(): Observable<any[]> {
    return this.httpClient.get<any[]>(this.url);
  }

  getReservationById(id: number): Observable<any> {
    return this.httpClient.get<any>(`${this.url}/${id}`);
  }

  updateReservation(id: number, reservation: any): Observable<any> {
  return this.httpClient.post<any>(`${this.url}/update/${id}`, reservation);
}

}