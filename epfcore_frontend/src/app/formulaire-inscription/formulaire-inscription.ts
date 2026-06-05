import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
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
  currentStep: number = 1;
  totalSteps: number = 3;

  constructor(
    private fb: FormBuilder,
    private formulaireService: FormulaireService,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    this.formulaireForm = this.fb.group({

      genre: [''],
      telephone: [null],
      nationalite: [''],
      adresse: [''],

      dernierDiplome: [''],
      etablissement: [''],
      anneeObtention: [null],

      programmeChoisi: [''],
      motivations: [''],
      campusVille: [''],
      anneeIntegration: [null],
      majeur: [''],
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

  nextStep(): void {
    if (this.currentStep < this.totalSteps) {
      this.currentStep++;
    }
  }

  prevStep(): void {
    if (this.currentStep > 1) {
      this.currentStep--;
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
      this.errorMessage = '';
      this.cdr.detectChanges();
    } catch (error) {
      this.errorMessage = 'Une erreur est survenue lors de la sauvegarde';
      this.successMessage = '';
      console.error(error);
    }
  }

  get completionStep1(): number {
    const fields = ['genre', 'telephone', 'nationalite', 'adresse'];
    const filled = fields.filter(f => this.formulaireForm.get(f)?.value).length;
    return Math.round((filled / fields.length) * 100);
  }

  get completionStep2(): number {
    const fields = ['dernierDiplome', 'etablissement', 'anneeObtention'];
    const filled = fields.filter(f => this.formulaireForm.get(f)?.value).length;
    return Math.round((filled / fields.length) * 100);
  }

  get completionStep3(): number {
    const fields = ['programmeChoisi', 'campusVille', 'anneeIntegration', 'majeur', 'motivations'];
    const filled = fields.filter(f => this.formulaireForm.get(f)?.value).length;
    return Math.round((filled / fields.length) * 100);
  }

  get completionTotal(): number {
    return Math.round((this.completionStep1 + this.completionStep2 + this.completionStep3) / 3);
  }

  get isFormComplete(): boolean {
    const fields = [
      'genre', 'telephone', 'nationalite', 'adresse',
      'dernierDiplome', 'etablissement', 'anneeObtention',
      'programmeChoisi', 'campusVille', 'anneeIntegration', 'majeur', 'motivations'
    ];
    return fields.every(f => this.formulaireForm.get(f)?.value);
  }
}
