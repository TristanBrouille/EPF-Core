import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CandidatService } from '../candidat/candidat-service';
import { RecaptchaService } from './recaptchaService';

@Component({
  selector: 'app-register-candidat',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register-candidat.html',
  styleUrl: './register-candidat.scss',
})
export class RegisterCandidat implements OnInit, OnDestroy {
  registerForm!: FormGroup;
  errorMessage: string = '';
  captchaValid: boolean = false;
  private captchaInterval: any;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private candidatService: CandidatService,
    protected recaptchaService: RecaptchaService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      birthDate: ['', Validators.required],
    });

    const script = document.createElement('script');
    script.src = 'https://www.google.com/recaptcha/api.js';
    script.onload = () => {
      (window as any).grecaptcha.ready(() => {
        (window as any).grecaptcha.render('recaptcha-container', {
          sitekey: '6LfxIhotAAAAAFq4j3YQAn7Gi7eguo8wSLlKqFek'
        });
      });
    };
    document.head.appendChild(script);

    this.captchaInterval = setInterval(() => {
      const isValid = this.recaptchaService.isValid();
      if (isValid !== this.captchaValid) {
        this.captchaValid = isValid;
        this.cdr.detectChanges(); // ← force Angular à mettre à jour le bouton
      }
    }, 500);
  }

  ngOnDestroy(): void {
    clearInterval(this.captchaInterval);
  }

  get isFormReady(): boolean {
    return this.registerForm.valid && this.captchaValid;
  }

  async onSubmit(): Promise<void> {
    if (this.registerForm.valid && this.captchaValid) {
      try {
        await this.candidatService.register({
          ...this.registerForm.value,
          captchaToken: this.recaptchaService.getToken()
        });
        await this.router.navigate(['/login']);
      } catch (error) {
        this.errorMessage = 'Une erreur est survenue lors de l\'inscription';
        this.recaptchaService.reset();
        console.error(error);
      }
    }
  }
}
