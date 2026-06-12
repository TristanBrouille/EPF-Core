import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { loginService } from '../login/loginService';
import { Router, RouterLink } from '@angular/router';
import { FormulaireService } from '../formulaire-inscription/formulaireService';

@Component({
  selector: 'app-candidat',
  imports: [RouterLink],
  templateUrl: './candidat.html',
  styleUrl: './candidat.scss',
})
export class Candidat implements OnInit {
  user: any = null;
  hasFormulaire: boolean = false;
  errorMessage: string = '';

  constructor(
    private authService: loginService,
    private formulaireService: FormulaireService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    try {
      this.user = await this.authService.me();
      await this.checkFormulaire();
      this.cdr.detectChanges();
    } catch (error) {
      this.errorMessage = 'Impossible de récupérer les informations';
      console.error(error);
    }
  }

  async checkFormulaire(): Promise<void> {
    try {
      await this.formulaireService.getMyFormulaire();
      this.hasFormulaire = true;
    } catch (error: any) {
      if (error.status === 404) {
        this.hasFormulaire = false;
      }
    }
  }

  async createFormulaire(): Promise<void> {
    try {
      await this.formulaireService.create({});
      this.hasFormulaire = true;
      await this.router.navigate(['/formulaire-inscription']);
    } catch (error) {
      this.errorMessage = 'Impossible de créer le dossier de candidature';
      console.error('Erreur lors de la création du dossier', error);
      this.cdr.detectChanges();
    }
  }

  steps = [
    { id: 1, label: 'Inscription' },
    { id: 2, label: 'Documents' },
    { id: 3, label: 'Vérification' },
    { id: 4, label: 'Validation' },
  ];

  currentStep: number = 1;

  get progressPercent(): number {
    return ((this.currentStep - 1) / (this.steps.length - 1)) * 100;
  }

  get currentStepMessage(): string {
    const messages: { [key: number]: string } = {
      1: 'Votre inscription est en cours.',
      2: 'En attente de vos documents.',
      3: 'Vos documents sont en cours de vérification.',
      4: 'Votre dossier a été validé !',
    };
    return messages[this.currentStep] ?? '';
  }
}
