import { Component } from '@angular/core';
import {MatIcon} from '@angular/material/icon';
import {MatButton} from '@angular/material/button';
import {MatCard} from '@angular/material/card';
import {loginService} from '../login/loginService';
import {User} from '../model/user';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-home',
  imports: [
    MatIcon,
    MatButton,
    MatCard
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {

  protected readonly user$: Observable<User>;

  constructor(private authService: loginService) {
    this.user$ = this.authService.me();
  }

}
