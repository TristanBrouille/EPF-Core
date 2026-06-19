import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CandidatService {
  private readonly baseUrl = environment.apiUrl;
  private url(path: string): string {
    return `${this.baseUrl}/${path}`;
  }
  private readonly httpClient = inject(HttpClient);

  register(candidat: any): Promise<any> {
    return firstValueFrom(
      this.httpClient.post(this.url('candidats/register'), candidat)
    );
  }
}
