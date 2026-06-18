import {Component, computed, signal} from '@angular/core';
import {MatListItem, MatNavList} from '@angular/material/list';
import {RouterLink} from '@angular/router';
import {MatIcon} from '@angular/material/icon';
import {loginService} from '../../login/loginService';
import {User} from '../../model/user';

@Component({
  selector: 'app-sidebar',
  imports: [
    MatNavList,
    MatListItem,
    RouterLink,
    MatIcon,
  ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar {

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
