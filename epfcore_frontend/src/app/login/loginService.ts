import {firstValueFrom, Observable, of, throwError} from 'rxjs';
import { delay } from 'rxjs/operators';
import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User, UserLog} from '../model/user';

@Injectable({
  providedIn: 'root',
})
export class loginService {

  readonly url = "http://localhost:8080/login"
  readonly urlMe = "http://localhost:8080/me";
  private readonly httpClient = inject(HttpClient);


  login(user: UserLog): Promise<any> {
    return firstValueFrom(
      this.httpClient.post(this.url, user, { observe: 'response' })
    );
  }

  me(): Promise<any>{
    return firstValueFrom(
      this.httpClient.get(this.urlMe));
  }
}
