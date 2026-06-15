import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { FormulaireService } from '../../formulaire-inscription/formulaireService';
import { Formulaire } from '../../model/formulaire';

@Component({
  selector: 'app-formulaires-list',
  imports: [DatePipe],
  templateUrl: './formulaires-list.html',
  styleUrl: './formulaires-list.scss',
})
export class FormulairesList implements OnInit {
  formulaires: Formulaire[] = [];
  errorMessage = '';

  constructor(
    private readonly formulaireService: FormulaireService,
    private readonly router: Router,
    private readonly cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    try {
      this.formulaires = await this.formulaireService.getAllSoumis();
    } catch (error) {
      this.errorMessage = 'Impossible de récupérer les dossiers de candidature';
      console.error(error);
    }
    this.cdr.detectChanges();
  }

  openFormulaire(id?: number): void {
    if (id) {
      this.router.navigate(['/admin/formulaires', id]);
    }
  }
}
