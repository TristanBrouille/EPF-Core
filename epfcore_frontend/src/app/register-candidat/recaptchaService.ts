import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class RecaptchaService {

  getToken(): string | null {
    const grecaptcha = (window as any).grecaptcha;
    if (!grecaptcha) {
      return null;
    }
    return grecaptcha.getResponse() || null;
  }

  reset(): void {
    const grecaptcha = (window as any).grecaptcha;
    if (grecaptcha) {
      grecaptcha.reset();
    }
  }

  isValid(): boolean {
    return this.getToken() !== null;
  }
}
