import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { delay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class loginService {
  private mockUser = {
    email: 'test@epf.fr',
    password: 'epf123',
  };

  constructor() {}

  login(email: string, password: string): Observable<any> {
    if (email === this.mockUser.email && password === this.mockUser.password) {
      return of({ token: 'fake-jwt-token', user: { email } }).pipe(delay(1000));
    } else {
      return throwError(() => new Error('Invalid credentials')).pipe(delay(1000));
    }
  }
}
