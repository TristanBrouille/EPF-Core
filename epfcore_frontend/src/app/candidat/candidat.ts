import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { loginService } from '../login/loginService';
import { Router, RouterLink } from '@angular/router';
import { FormulaireService } from '../formulaire-inscription/formulaireService';
import { Formulaire } from '../model/formulaire';

@Component({
  selector: 'app-candidat',
  imports: [RouterLink],
  templateUrl: './candidat.html',
  styleUrl: './candidat.scss',
})
export class Candidat implements OnInit {
  user: any = null;
  hasFormulaire: boolean = false;
  formulaire: Formulaire | null = null;
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
      this.formulaire = await this.formulaireService.getMyFormulaire();
      this.hasFormulaire = true;
      if (this.formulaire.soumis) {
        this.currentStep = 2;
      }
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
    { id: 1, label: 'Formulaire de candidature' },
    { id: 2, label: 'Entretien' },
    { id: 3, label: 'Décision du jury' },
    { id: 4, label: 'Validation' },
  ];

  currentStep: number = 1;

  get isSoumis(): boolean {
    return !!this.formulaire?.soumis;
  }

  get progressPercent(): number {
    return ((this.currentStep - 1) / (this.steps.length - 1)) * 100;
  }

  get currentStepMessage(): string {
    const messages: { [key: number]: string } = {
      1: 'Remplissage et soumission du dossier de candidature.',
      2: 'Un entretien sera prévu bientôt.',
      3: 'Le jury est en concertation pour votre dossier.',
      4: 'Vous avez rçue une réponse par mail du jury',
    };
    return messages[this.currentStep] ?? '';
  }
}
