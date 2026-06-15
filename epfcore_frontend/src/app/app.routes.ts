import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Candidat } from './candidat/candidat';
import { RegisterCandidat } from './register-candidat/register-candidat';
import { Home } from './home/home';
import { FormulaireInscription } from './formulaire-inscription/formulaire-inscription';
import { StudentProfile } from './student-profile/student-profile';
import { HistoryStudent } from './history-student/history-student';
import { StudentList } from './student-list/student-list';
import { StudentSearch } from './student-search/student-search';


export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'candidat', component: Candidat },
  { path: 'register-candidat', component: RegisterCandidat },
  { path: 'home', component: Home },
  { path: 'formulaire-inscription', component: FormulaireInscription },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'student-profile', component: StudentProfile },
  { path: 'history-student', component: HistoryStudent },
  { path: 'student-list', component: StudentList },
  { path: 'student-search/:id', component: StudentSearch }
];
