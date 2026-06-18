import {Component, computed, signal} from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { MatCard } from '@angular/material/card';
import {loginService} from '../login/loginService';
import {User} from '../model/user';

@Component({
  selector: 'app-home',
  imports: [RouterLink, MatIcon, MatButton, MatCard],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {

  public user = signal<User | undefined>(undefined);

  constructor(private readonly loginService : loginService) {}

  async ngOnInit() {
    try {
      const userData = await this.loginService.me();
      this.user.set(userData);
    } catch (error) {
      console.error("Erreur lors de la récupération de l'utilisateur", error);
      this.user.set(undefined);
    }
  }

  public isGestionnaireAdmission = computed(() => {
    return this.user()?.role?.includes('GESTIONNAIRE_ADMISSION') ?? false;
  });

  public isAdmin = computed(() => {
    return this.user()?.role?.includes('ADMIN') ?? false;
  });
}
