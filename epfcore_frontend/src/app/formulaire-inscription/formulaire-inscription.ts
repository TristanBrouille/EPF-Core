import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Formulaire } from '../model/formulaire';
import { FormulaireService } from './formulaireService';

@Component({
  selector: 'app-formulaire-inscription',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './formulaire-inscription.html',
  styleUrl: './formulaire-inscription.scss',
})
export class FormulaireInscription implements OnInit {
  formulaireForm!: FormGroup;
  isExisting: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private formulaireService: FormulaireService,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    this.formulaireForm = this.fb.group({
      dernierDiplome: [''],
      etablissement: [''],
      niveauEtude: [''],
      anneeObtention: [null],
      programmeChoisi: [''],
      campusChoisi: [''],
      motivations: [''],
    });

    try {
      const existing = await this.formulaireService.getMyFormulaire();
      this.isExisting = true;
      this.formulaireForm.patchValue(existing);
      this.cdr.detectChanges();
    } catch (error: any) {
      if (error.status !== 404) {
        console.error(error);
      }
    }
  }

  async onSubmit(): Promise<void> {
    const formulaire: Formulaire = this.formulaireForm.value;

    try {
      if (this.isExisting) {
        await this.formulaireService.update(formulaire);
      } else {
        await this.formulaireService.create(formulaire);
        this.isExisting = true;
      }
      this.successMessage = 'Dossier sauvegardé avec succès !';
      this.cdr.detectChanges();
    } catch (error) {
      this.errorMessage = 'Une erreur est survenue lors de la sauvegarde';
      console.error(error);
    }
  }
}
