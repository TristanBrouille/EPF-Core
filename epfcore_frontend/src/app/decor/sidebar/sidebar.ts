import { Component } from '@angular/core';
import {MatListItem, MatNavList} from '@angular/material/list';
import {RouterLink} from '@angular/router';
import {MatIcon} from '@angular/material/icon';
import {AuthState} from '../../auth/auth-state';

@Component({
  selector: 'app-sidebar',
  imports: [
    MatNavList,
    MatListItem,
    RouterLink,
    MatIcon
  ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar {

  constructor(private readonly authState: AuthState) {}

  get isAdminCandidature(): boolean {
    return this.authState.hasRole('ADMIN_CANDIDATURE');
  }
}
