import { Injectable } from '@angular/core';

const ROLES_KEY = 'roles';

@Injectable({
  providedIn: 'root',
})
export class AuthState {

  setRoles(roles: string[]): void {
    sessionStorage.setItem(ROLES_KEY, JSON.stringify(roles));
  }

  getRoles(): string[] {
    const raw = sessionStorage.getItem(ROLES_KEY);
    return raw ? JSON.parse(raw) : [];
  }

  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }

  clear(): void {
    sessionStorage.removeItem(ROLES_KEY);
  }
}
