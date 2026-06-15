import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EntretienService } from '../entretien-service';
import { EntretienCandidature, StatutEntretien } from '../../model/entretien';

@Component({
  selector: 'app-entretien-detail',
  imports: [DatePipe, FormsModule],
  templateUrl: './entretien-detail.html',
  styleUrl: './entretien-detail.scss',
})
export class EntretienDetail implements OnInit {
  entretien: EntretienCandidature | null = null;
  errorMessage = '';
  successMessage = '';

  readonly statutOptions: StatutEntretien[] = ['PLANIFIE', 'REALISE', 'ANNULE', 'NO_SHOW'];

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly entretienService: EntretienService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    try {
      this.entretien = await this.entretienService.getById(id);
    } catch (error) {
      this.errorMessage = "Impossible de récupérer l'entretien";
      console.error(error);
    }
    this.cdr.detectChanges();
  }

  async updateStatut(): Promise<void> {
    if (!this.entretien?.id) {
      return;
    }
    try {
      this.entretien = await this.entretienService.update(this.entretien.id, this.entretien);
      this.successMessage = 'Statut mis à jour';
      this.errorMessage = '';
    } catch (error) {
      this.errorMessage = 'Impossible de mettre à jour le statut';
      this.successMessage = '';
      console.error(error);
    }
    this.cdr.detectChanges();
  }

  async deleteEntretien(): Promise<void> {
    if (!this.entretien?.id) {
      return;
    }
    if (!confirm('Supprimer cet entretien ?')) {
      return;
    }
    try {
      await this.entretienService.delete(this.entretien.id);
      await this.router.navigate(['/admin/entretiens']);
    } catch (error) {
      this.errorMessage = "Impossible de supprimer l'entretien";
      console.error(error);
    }
    this.cdr.detectChanges();
  }

  goBack(): void {
    this.router.navigate(['/admin/entretiens']);
  }
}
