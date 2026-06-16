import { Component, OnInit } from '@angular/core';
import { MatToolbar } from '@angular/material/toolbar';
import { MatIcon } from '@angular/material/icon';
import {MatButton, MatIconButton} from '@angular/material/button';
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu';
import { loginService } from '../../login/loginService';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ImpersonateDialogComponent } from '../../impersonate-dialog/impersonate-dialog.component';
import { ImpersonateService } from '../../impersonate-dialog/impersonate.service';
import { CommonModule } from '@angular/common';
import {AuthState} from '../../auth/auth-state';


@Component({
  selector: 'app-header',
  imports: [
    CommonModule,
    MatToolbar,
    MatIcon,
    MatIconButton,
    MatMenuTrigger,
    MatMenu,
    MatMenuItem,
    MatButton,
  ],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header implements OnInit {
  protected isInside = false;
  role : string | undefined;
  isAdmin = false;
  isImpersonating = false;

  constructor(
    protected readonly router: Router,
    private readonly loginService: loginService,
    public dialog: MatDialog,
    private impersonateService: ImpersonateService,
    private readonly authState: AuthState
  ) {}

  ngOnInit(): void {
    console.log("prout");
    this.loginService.me().then(user => {
      this.role = user.role;
      this.isAdmin = this.role.includes('ADMIN') ?? false;
      this.isImpersonating = localStorage.getItem('isImpersonating') === 'true';
    }).catch(err => {
      console.error('Failed to get current user info', err);
    });


  }

  protected async logout() {
      await this.loginService.logout();
      this.authState.clear();
      localStorage.clear();
      try{
        await this.router.navigate(['/login']);
      }catch(error){
        console.error(error);
      }
  }

  protected checkClose() {
    this.isInside = false;
  }

  protected closeMenu(trigger: any) {
    if (!this.isInside) {
      trigger.closeMenu();
    }
  }

  goToProfile() {
    const url = this.router.url;
    if (url === '/candidat' || url === '/formulaire-inscription') {
      this.router.navigate(['/candidat']);
    } else {
      this.router.navigate(['/student-profile']);
    }
  }

  openImpersonateDialog(): void {
    const dialogRef = this.dialog.open(ImpersonateDialogComponent, {
      width: '400px'
    });
  }

  async revertImpersonation(): Promise<void> {
    try {
      await this.impersonateService.revert();
    } catch (error) {
      console.error('Failed to revert impersonation in component', error);
    }
  }
}
