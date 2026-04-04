import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import { CandidatService } from '../services/candidat-service';

@Component({
  selector: 'app-register-candidat',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register-candidat.html',
  styleUrl: './register-candidat.scss',
})
export class RegisterCandidat implements OnInit {
  registerForm!: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private candidatService: CandidatService
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
      try {
        await this.candidatService.register(this.registerForm.value);
        this.router.navigate(['/login']);
      } catch (error) {
        this.errorMessage = 'Une erreur est survenue lors de l\'inscription';
        console.error(error);
      }
    }
  }
}
