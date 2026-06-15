import { Routes } from '@angular/router';
import {Login} from './login/login';
import {Candidat} from './candidat/candidat';
import {RegisterCandidat} from './register-candidat/register-candidat';
import {Home} from './home/home';
import {FormulaireInscription} from './formulaire-inscription/formulaire-inscription';
import { StudentProfile } from './student-profile/student-profile';
import { FormulairesList } from './admin-candidature/formulaires-list/formulaires-list';
import { FormulaireDetail } from './admin-candidature/formulaire-detail/formulaire-detail';
import { EntretiensSchedule } from './admin-candidature/entretiens-schedule/entretiens-schedule';
import { EntretienDetail } from './admin-candidature/entretien-detail/entretien-detail';
import { EntretienForm } from './admin-candidature/entretien-form/entretien-form';

export const routes: Routes = [
  {path: 'login', component: Login},
  {path: 'candidat', component: Candidat},
  {path: 'register-candidat', component: RegisterCandidat},
  {path: 'home', component: Home},
  {path: 'formulaire-inscription', component: FormulaireInscription},
  {path: '', redirectTo: 'home', pathMatch: 'full' },
  {path: 'student-profile', component: StudentProfile },
  {path: 'admin/formulaires', component: FormulairesList},
  {path: 'admin/formulaires/:id', component: FormulaireDetail},
  {path: 'admin/entretiens', component: EntretiensSchedule},
  {path: 'admin/entretiens/nouveau', component: EntretienForm},
  {path: 'admin/entretiens/:id', component: EntretienDetail}
];
