import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AdminService} from '../admin.service';
import {NgClass} from '@angular/common';
import {Router} from '@angular/router';

@Component({
  selector: 'app-registration',
  imports: [
    ReactiveFormsModule,
    NgClass
  ],
  templateUrl: './registration.html',
  styleUrl: './registration.scss',
})
export class Registration implements OnInit {

  registerForm!: FormGroup;
  isSubmitting = false;
  message: string | null = null;
  isError = false;

  roles: string[] = [];

  constructor(
    private fb: FormBuilder,
    private registrationService: AdminService,
    private router : Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.registerForm = this.fb.group({
      firstname: ['', [Validators.required, Validators.minLength(2)]],
      lastname: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      birthDate: ['', Validators.required],
      role: ['', Validators.required],
      idRfid: ['', [Validators.required, Validators.pattern(/^RF\d+$/)]]
    });

    try {
      this.roles = await this.registrationService.roles();

      if (this.roles.length > 0) {
        this.registerForm.patchValue({ role: this.roles[0] });
      }
    } catch (error) {
      console.error("Erreur lors de la récupération des rôles du back :", error);
    }
  }

  async onSubmit(): Promise<void> {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.message = null;

    try {
      const response = await this.registrationService.register(this.registerForm.value);

      this.isSubmitting = false;
      this.isError = false;
      this.message = "Inscription réussie !";
      this.registerForm.reset({ role: 'USER' });

    } catch (error) {
      this.isSubmitting = false;
      this.isError = true;
      this.message = "Une erreur est survenue lors de l'inscription.";
      console.error('Erreur lors du register :', error);
    }
  }

  return(): void {
    this.router.navigate(['/user-list']);
  }
}
