import { Component } from '@angular/core';
import {MatIcon} from '@angular/material/icon';
import {MatButton} from '@angular/material/button';
import {MatCard} from '@angular/material/card';

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

}
