import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {firstValueFrom} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CandidatService {
  readonly url = 'http://localhost:8080/candidats/register';
  private readonly httpClient = inject(HttpClient);

  register(candidat: any): Promise<any> {
    return firstValueFrom(
      this.httpClient.post(this.url, candidat)
    );
  }
}
