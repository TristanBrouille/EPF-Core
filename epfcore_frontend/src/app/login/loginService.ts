import {firstValueFrom} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User, UserLog} from '../model/user';

@Injectable({
  providedIn: 'root',
})
export class loginService {

  private readonly baseUrl = "http://localhost:8080";
  constructor(private httpClient: HttpClient) {}

  private url(path: string): string {
    return `${this.baseUrl}/${path}`;
  }

  login(user: UserLog): Promise<any> {
    return firstValueFrom(
      this.httpClient.post(this.url('login'), user, { observe: 'response' })
    );
  }

  logout(): Promise<void> {
    return firstValueFrom(
      this.httpClient.post<void>(this.url('logout'), {})
    );
  }

  me(): Promise<User> {
    return firstValueFrom(
      this.httpClient.get<User>(this.url('me'))
    );
  }
}
