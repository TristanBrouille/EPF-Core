import { Routes } from '@angular/router';
import {Login} from './login/login';
import {Home} from './home/home';

export const routes: Routes = [
  {path: 'home', component: Home},
  {path: 'login', component: Login},
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home' }
];
