import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RegistrationData, User, UserAdmin, UserDto} from '../model/user';
import {firstValueFrom} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AdminService {

  private readonly baseUrl = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) {}

  private url(path: string): string {
    return `${this.baseUrl}/${path}`;
  }

  async register(data: RegistrationData): Promise<any> {
    return firstValueFrom(this.httpClient.post<any>(this.url('register'), data));
  }

  async roles(): Promise<string[]>{
    return firstValueFrom(
      this.httpClient.get<string[]>(this.url('admin/users/roles'))
    );
  }

  async updateUser(id: number, data: UserAdmin): Promise<UserAdmin> {
    return firstValueFrom(this.httpClient.patch<UserAdmin>(this.url(`admin/users/${id}`), data));
  }
  async getAllUsers(): Promise<UserDto[]> {
    return firstValueFrom(this.httpClient.get<UserDto[]>(this.url('admin/users')));
  }

  async getUserById(id: number): Promise<UserAdmin> {
    return firstValueFrom(this.httpClient.get<UserAdmin>(this.url(`admin/users/${id}`)));
  }
}
