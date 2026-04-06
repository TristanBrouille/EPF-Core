import { Routes } from '@angular/router';
import {Dashboard} from './dashboard/dashboard';
import {Login} from './login/login';

export const routes: Routes = [
  {path: 'dashboard', component: Dashboard},
  {path: 'login', component: Login}
];
