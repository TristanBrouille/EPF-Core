import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { loginService } from '../login/loginService';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-candidat',
  imports: [CommonModule],
  templateUrl: './candidat.html',
  styleUrl: './candidat.scss',
})
export class Candidat implements OnInit {
  user: any = null;
  errorMessage: string = '';

  constructor(
    private authService: loginService,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    try {
      this.user = await this.authService.me();
      this.cdr.detectChanges();
    } catch (error) {
      this.errorMessage = 'Impossible de récupérer les informations';
      console.error(error);
    }
  }

  steps = [
    { id: 1, label: 'Inscription' },
    { id: 2, label: 'Documents' },
    { id: 3, label: 'Vérification' },
    { id: 4, label: 'Validation' },
  ];

  currentStep: number = 1; // à modifier selon l'avancement réel du candidat

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
