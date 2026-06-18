import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { loginService } from '../login/loginService';
import { Router, RouterLink } from '@angular/router';
import { FormulaireService } from '../formulaire-inscription/formulaireService';
import { Formulaire } from '../model/formulaire';
import { EntretienService } from '../gestionnaire-admission/entretien-service';
import { EntretienCandidature } from '../model/entretien';

@Component({
  selector: 'app-candidat',
  imports: [RouterLink, DatePipe, FormsModule],
  templateUrl: './candidat.html',
  styleUrl: './candidat.scss',
})
export class Candidat implements OnInit {
  user: any = null;
  hasFormulaire: boolean = false;
  rgpdAccepte: boolean = false;
  formulaire: Formulaire | null = null;
  entretienPlanifie: EntretienCandidature | null = null;
  entretienRealise: EntretienCandidature | null = null;
  errorMessage: string = '';

  get entretienActif(): EntretienCandidature | null {
    return this.entretienRealise ?? this.entretienPlanifie;
  }

  constructor(
    private authService: loginService,
    private formulaireService: FormulaireService,
    private entretienService: EntretienService,
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
      if (this.formulaire.decisionAdmission === 'REFUSE') {
        this.currentStep = 4;
      } else if (this.formulaire.soumis) {
        this.currentStep = 2;
        await this.checkEntretien();
      }
    } catch (error: any) {
      if (error.status === 404) {
        this.hasFormulaire = false;
      }
    }
  }

  async checkEntretien(): Promise<void> {
    try {
      const entretiens = await this.entretienService.getMesEntretiens();
      this.entretienPlanifie = entretiens.find(e => e.statut === 'PLANIFIE') ?? null;
      this.entretienRealise = entretiens.find(e => e.statut === 'REALISE') ?? null;
      if (this.entretienRealise) {
        this.currentStep = 3;
      }
    } catch {
      // silencieux : l'absence d'entretien n'est pas une erreur
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

  get isRefuse(): boolean {
    return this.formulaire?.decisionAdmission === 'REFUSE';
  }

  get progressPercent(): number {
    return ((this.currentStep - 1) / (this.steps.length - 1)) * 100;
  }

  get currentStepMessage(): string {
    if (this.currentStep === 2) {
      return this.entretienPlanifie ? 'Un entretien est prévu.' : 'Un entretien sera prévu bientôt.';
    }
    const messages: { [key: number]: string } = {
      1: 'Remplissage et soumission du dossier de candidature.',
      3: 'Le jury va prendre une décision.',
      4: 'Vous avez reçu une réponse par mail du jury.',
    };
    return messages[this.currentStep] ?? '';
  }
}
