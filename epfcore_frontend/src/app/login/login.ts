
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {loginService} from './loginService';
import {UserLog} from '../model/user';
import {Router, RouterLink} from '@angular/router';
import {AuthState} from '../auth/auth-state';


@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})

export class Login implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string = '';

  constructor(protected readonly router : Router, private fb: FormBuilder, private authService: loginService, private authState: AuthState) {}

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
        const authorities: string[] = response.body?.roles ?? [];
        this.authState.setRoles(authorities);
          setTimeout(() => {
            if (authorities.includes('CANDIDAT')) {
              this.router.navigate(['/candidat']);
            } else if (authorities.includes('GESTIONNAIRE_ADMISSION')) {
              this.router.navigate(['/admin/formulaires']);
            } else {
              this.router.navigate(['/home']);
            }
          }, 500);


      } catch (error) {
        console.error(error);
      }
    }
  }

}
