import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {loginService} from '../login/loginService';
import {FormBuilder} from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {

  constructor(protected router : Router, private fb: FormBuilder, private authService: loginService) {}

  async test(){
      await this.authService.me();
  }

}
