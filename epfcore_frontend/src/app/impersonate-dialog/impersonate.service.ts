import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { tap, catchError, of, lastValueFrom} from 'rxjs';
import { UserDto } from '../model/user';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ImpersonateService {

  private readonly baseUrl = environment.apiUrl;

  constructor(private http: HttpClient, private snackBar: MatSnackBar) { }

  private url(path: string): string {
    return `${this.baseUrl}/admin/${path}`;
  }

  async impersonate(userId: number): Promise<any> {
    const request$ = this.http.post(this.url(`impersonate/${userId}`), {}).pipe(
      tap(() => {
        localStorage.setItem('isImpersonating', 'true');
        window.location.reload();
      }),
      catchError(err => {
        console.error('Failed to impersonate user', err);
        this.snackBar.open('Failed to impersonate user', 'Close', { duration: 3000 });
        return of(null);
      })
    );

    return lastValueFrom(request$);
  }

  async revert(): Promise<any> {
    const request$ = this.http.post(this.url(`impersonate/revert`), {}).pipe(
      tap(() => {
        localStorage.removeItem('isImpersonating');
        window.location.reload();
      }),
      catchError(err => {
        console.error('Failed to revert impersonation', err);
        this.snackBar.open('Failed to revert impersonation', 'Close', { duration: 3000 });
        return of(null);
      })
    );

    return lastValueFrom(request$);
  }

  async getAllUsers(): Promise<UserDto[]> {
    const request$ = this.http.get<UserDto[]>(this.url(`users`)).pipe(
      catchError(err => {
        console.error('Failed to fetch users', err);
        this.snackBar.open('Failed to fetch users', 'Close', { duration: 3000 });
        return of([]);
      })
    );

    return lastValueFrom(request$);
  }
}
