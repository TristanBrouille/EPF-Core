import { Component } from '@angular/core';
import {MatToolbar} from '@angular/material/toolbar';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';
import {loginService} from '../../login/loginService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [
    MatToolbar,
    MatIcon,
    MatIconButton,
    MatMenuTrigger,
    MatMenu,
    MatMenuItem,
  ],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  protected isInside = false;

  constructor(protected readonly router : Router,private readonly loginService: loginService) {}

  protected async logout() {
      await this.loginService.logout();
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
    this.router.navigate(['/student-profile']);
  }

  

}
