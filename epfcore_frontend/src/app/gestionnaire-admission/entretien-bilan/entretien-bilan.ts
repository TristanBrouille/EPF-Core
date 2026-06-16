import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EntretienService } from '../entretien-service';
import { EntretienCandidature } from '../../model/entretien';
import { FormulaireService } from '../../formulaire-inscription/formulaireService';
import { DecisionAdmission } from '../../model/formulaire';

@Component({
  selector: 'app-entretien-bilan',
  imports: [FormsModule, DatePipe],
  templateUrl: './entretien-bilan.html',
  styleUrl: './entretien-bilan.scss',
})
export class EntretienBilan implements OnInit {
  entretien: EntretienCandidature | null = null;
  note: number | null = null;
  commentaire = '';
  selectedDecision: DecisionAdmission = 'EN_ATTENTE';
  errorMessage = '';
  successMessage = '';

  readonly decisionOptions: DecisionAdmission[] = ['EN_ATTENTE', 'ADMIS', 'REFUSE'];

  get noteInvalide(): boolean {
    return this.note !== null && (this.note < 0 || this.note > 20);
  }

  get decisionVerrouillee(): boolean {
    return this.entretien?.decisionAdmission === 'ADMIS' || this.entretien?.decisionAdmission === 'REFUSE';
  }

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly entretienService: EntretienService,
    private readonly formulaireService: FormulaireService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    try {
      this.entretien = await this.entretienService.getById(id);
      this.note = this.entretien.note ?? null;
      this.commentaire = this.entretien.commentaire ?? '';
      this.selectedDecision = this.entretien.decisionAdmission ?? 'EN_ATTENTE';
    } catch (error) {
      this.errorMessage = "Impossible de récupérer l'entretien";
      console.error(error);
    }
    this.cdr.detectChanges();
  }

  async onSubmit(): Promise<void> {
    if (!this.entretien?.id || this.noteInvalide) {
      return;
    }
    try {
      await this.entretienService.update(this.entretien.id, {
        ...this.entretien,
        note: this.note,
        commentaire: this.commentaire,
      });
      this.successMessage = 'Bilan enregistré';
      this.errorMessage = '';
      this.cdr.detectChanges();
      setTimeout(() => this.router.navigate(['/admin/entretiens', this.entretien!.id]), 1200);
    } catch (error) {
      this.errorMessage = "Impossible d'enregistrer le bilan";
      this.successMessage = '';
      console.error(error);
    }
    this.cdr.detectChanges();
  }

  async confirmerDecision(): Promise<void> {
    if (!this.entretien?.formulaireId) {
      return;
    }
    const label = this.selectedDecision === 'ADMIS' ? 'admis' : this.selectedDecision === 'REFUSE' ? 'refusé' : 'en attente';
    if (!confirm(`Confirmer la décision "${label}" pour ce candidat ? Cette action sera définitive.`)) {
      return;
    }
    try {
      const formulaireMaj = await this.formulaireService.updateDecision(this.entretien.formulaireId, this.selectedDecision);
      this.entretien = { ...this.entretien, decisionAdmission: formulaireMaj.decisionAdmission };
      this.successMessage = 'Décision enregistrée';
      this.errorMessage = '';
    } catch (error) {
      this.errorMessage = "Impossible d'enregistrer la décision";
      this.successMessage = '';
      console.error(error);
    }
    this.cdr.detectChanges();
  }

  goBack(): void {
    this.router.navigate(['/admin/entretiens', this.entretien?.id]);
  }
}
