import {firstValueFrom, Observable} from 'rxjs';
import {inject, Injectable} from '@angular/core';
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

  me(): Observable<User>{
    return this.httpClient.get<User>(this.urlMe);
  }
}
