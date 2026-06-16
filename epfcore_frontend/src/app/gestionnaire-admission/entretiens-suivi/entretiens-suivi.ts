import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { EntretienService } from '../entretien-service';
import { EntretienCandidature } from '../../model/entretien';
import { FormulaireService } from '../../formulaire-inscription/formulaireService';

@Component({
  selector: 'app-entretiens-suivi',
  imports: [DatePipe],
  templateUrl: './entretiens-suivi.html',
  styleUrl: './entretiens-suivi.scss',
})
export class EntretiensSuivi implements OnInit {
  retardes: EntretienCandidature[] = [];
  annulesEtNoShow: EntretienCandidature[] = [];
  sansDecision: EntretienCandidature[] = [];
  errorMessage = '';

  constructor(
    private readonly entretienService: EntretienService,
    private readonly formulaireService: FormulaireService,
    private readonly router: Router,
    private readonly cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    try {
      const tous = await this.entretienService.getAll();
      const maintenant = new Date();

      this.retardes = tous.filter(
        e => e.statut === 'PLANIFIE' && new Date(e.dateHeure) < maintenant
      );

      this.annulesEtNoShow = tous.filter(
        e => (e.statut === 'ANNULE' || e.statut === 'NO_SHOW') &&
          (e.decisionAdmission === 'EN_ATTENTE' || e.decisionAdmission == null)
      );

      this.sansDecision = tous.filter(
        e => e.statut === 'REALISE' &&
          (e.decisionAdmission === 'EN_ATTENTE' || e.decisionAdmission == null)
      );
    } catch (error) {
      this.errorMessage = 'Impossible de récupérer les entretiens';
      console.error(error);
    }
    this.cdr.detectChanges();
  }

  formatStatut(statut?: string): string {
    const labels: Record<string, string> = {
      PLANIFIE: 'planifié',
      REALISE: 'réalisé',
      ANNULE: 'annulé',
      NO_SHOW: 'non assisté',
    };
    return statut ? (labels[statut] ?? statut) : '';
  }

  openEntretien(id?: number): void {
    if (id) {
      this.router.navigate(['/admin/entretiens', id]);
    }
  }

  replanifier(e: EntretienCandidature, event: MouseEvent): void {
    event.stopPropagation();
    this.router.navigate(['/admin/entretiens/nouveau'], {
      queryParams: { formulaireId: e.formulaireId },
    });
  }

  async refuser(e: EntretienCandidature, event: MouseEvent): Promise<void> {
    event.stopPropagation();
    if (!e.formulaireId) {
      return;
    }
    const nom = `${e.candidat?.firstname ?? ''} ${e.candidat?.lastname ?? ''}`.trim();
    if (!confirm(`Refuser la candidature de ${nom} ? Cette décision sera définitive.`)) {
      return;
    }
    try {
      await this.formulaireService.updateDecision(e.formulaireId, 'REFUSE');
      this.annulesEtNoShow = this.annulesEtNoShow.filter(x => x.id !== e.id);
      this.cdr.detectChanges();
    } catch (error) {
      this.errorMessage = "Impossible d'enregistrer la décision";
      console.error(error);
      this.cdr.detectChanges();
    }
  }
}
