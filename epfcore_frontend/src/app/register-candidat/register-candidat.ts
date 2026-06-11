import { Component, OnInit, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import { CandidatService } from '../candidat/candidat-service';
import { RecaptchaService } from './recaptchaService';

@Component({
  selector: 'app-register-candidat',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register-candidat.html',
  styleUrl: './register-candidat.scss',
})
export class RegisterCandidat implements OnInit, AfterViewInit {
  registerForm!: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private candidatService: CandidatService,
    protected recaptchaService: RecaptchaService
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      birthDate: ['', Validators.required],
    });
  }

  async onSubmit(): Promise<void> {
    if (this.registerForm.valid) {
      if (!this.recaptchaService.isValid()) {
        this.errorMessage = 'Veuillez valider le captcha';
        return;
      }

      try {
        await this.candidatService.register({
          ...this.registerForm.value,
          captchaToken: this.recaptchaService.getToken()
        });
        this.router.navigate(['/login']);
      } catch (error) {
        this.errorMessage = 'Une erreur est survenue lors de l\'inscription';
        this.recaptchaService.reset();
        console.error(error);
      }
    }
  }

  ngAfterViewInit(): void {
    const grecaptcha = (window as any).grecaptcha;
    if (grecaptcha) {
      grecaptcha.render('recaptcha-container', {
        sitekey: '6LfxIhotAAAAAFq4j3YQAn7Gi7eguo8wSLlKqFek'
      });
    }
  }
}
