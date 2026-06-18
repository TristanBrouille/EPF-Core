import { Routes } from '@angular/router';
import { BulletinComponent } from './bulletin.component/bulletin.component';
import { CarnetNotesComponent } from './carnetNotes/carnetNotes';
import { Login } from './login/login';
import { Candidat } from './candidat/candidat';
import { RegisterCandidat } from './register-candidat/register-candidat';
import { Home } from './home/home';
import { FormulaireInscription } from './formulaire-inscription/formulaire-inscription';
import { StudentProfile } from './student-profile/student-profile';
import { HistoryStudent } from './history-student/history-student';
import { StudentList } from './student-list/student-list';
import { StudentSearch } from './student-search/student-search';
import { FormulairesList } from './gestionnaire-admission/formulaires-list/formulaires-list';
import { FormulaireDetail } from './gestionnaire-admission/formulaire-detail/formulaire-detail';
import { EntretiensSchedule } from './gestionnaire-admission/entretiens-schedule/entretiens-schedule';
import { EntretienDetail } from './gestionnaire-admission/entretien-detail/entretien-detail';
import { EntretienForm } from './gestionnaire-admission/entretien-form/entretien-form';
import { EntretienBilan } from './gestionnaire-admission/entretien-bilan/entretien-bilan';
import { EntretiensSuivi } from './gestionnaire-admission/entretiens-suivi/entretiens-suivi';
import { ArchiveDocuments } from './archive-documents/archive-documents';

export const routes: Routes = [
  {path: 'bulletin', component: BulletinComponent},
  {path: 'carnetnotes', component: CarnetNotesComponent},
  { path: '**', redirectTo: 'login' },
  {path: 'login', component: Login},
  {path: 'candidat', component: Candidat},
  {path: 'register-candidat', component: RegisterCandidat},
  {path: 'home', component: Home},
  {path: 'formulaire-inscription', component: FormulaireInscription},
  {path: '', redirectTo: 'home', pathMatch: 'full' },
  {path: 'history-student', component: HistoryStudent },
  {path: 'student-profile', component: StudentProfile },
  {path: 'student-list', component: StudentList },
  {path: 'student-search/:id', component: StudentSearch },
  {path: 'archive-documents', component: ArchiveDocuments },
  {path: 'admin/formulaires', component: FormulairesList},
  {path: 'admin/formulaires/:id', component: FormulaireDetail},
  {path: 'admin/entretiens', component: EntretiensSchedule},
  {path: 'admin/entretiens/suivi', component: EntretiensSuivi},
  {path: 'admin/entretiens/nouveau', component: EntretienForm},
  {path: 'admin/entretiens/:id/bilan', component: EntretienBilan},
  {path: 'admin/entretiens/:id', component: EntretienDetail},
];
