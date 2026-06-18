import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { MatCard } from '@angular/material/card';
import { AuthState } from '../auth/auth-state';

@Component({
  selector: 'app-home',
  imports: [RouterLink, MatIcon, MatButton, MatCard],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  constructor(private authState: AuthState) {}

  get isGestionnaireAdmission(): boolean {
    return this.authState.hasRole('GESTIONNAIRE_ADMISSION');
  }

  get isAdmin(): boolean {
    return this.authState.hasRole('ADMIN');
  }
}
