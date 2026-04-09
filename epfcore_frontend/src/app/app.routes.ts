import { Routes } from '@angular/router';
import {Login} from './login/login';
import {Candidat} from './candidat/candidat';
import {RegisterCandidat} from './register-candidat/register-candidat';
import {Home} from './home/home';
import { FicheEtudiant } from './fiche-etudiant/fiche-etudiant';

export const routes: Routes = [
  {path: 'login', component: Login},
  {path: 'candidat', component: Candidat},
  {path: 'register-candidat', component: RegisterCandidat},
  {path: 'home', component: Home},
  {path: '', redirectTo: 'home', pathMatch: 'full' },
];
