import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {loginService} from './loginService';
import {UserLog} from '../object/user';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  loginForm!: FormGroup;
  errorMessage: string = '';

  constructor(protected readonly router : Router, private fb: FormBuilder, private authService: loginService) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  async onSubmit(): Promise<void> {
    if (this.loginForm.valid) {
      const user: UserLog = this.loginForm.value;

      try {
        const response = await this.authService.login(user);
          setTimeout(() => {
            this.router.navigate(['/dashboard']);
          }, 500);


      } catch (error) {
        console.error(error);
      }
    }
  }

}
