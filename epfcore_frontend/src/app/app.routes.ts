import { Routes } from '@angular/router';
import {Login} from './login/login';
import {Candidat} from './candidat/candidat';
import {RegisterCandidat} from './register-candidat/register-candidat';
import {Home} from './home/home';
import {FormulaireInscription} from './formulaire-inscription/formulaire-inscription';

export const routes: Routes = [
  {path: 'login', component: Login},
  {path: 'candidat', component: Candidat},
  {path: 'register-candidat', component: RegisterCandidat},
  {path: 'home', component: Home},
  {path: 'formulaire-inscription', component: FormulaireInscription},
  {path: '', redirectTo: 'home', pathMatch: 'full' },
];
