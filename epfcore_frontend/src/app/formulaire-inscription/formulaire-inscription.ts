import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Formulaire } from '../model/formulaire';
import { FormulaireService } from './formulaireService';
import { DocumentService } from './documentService';
import { DocumentFormulaire } from '../model/document-formulaire';

export interface DocumentTypeDef {
  key: string;
  label: string;
}

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
  totalSteps: number = 4;

  formulaireId?: number;
  uploadedDocuments: Map<string, DocumentFormulaire> = new Map();
  selectedFiles: { [key: string]: File | null } = {};

  readonly obtentionYears: number[] = Array.from(
    { length: new Date().getFullYear() - 1900 + 1 },
    (_, i) => new Date().getFullYear() - i
  );

  readonly documentTypes: DocumentTypeDef[] = [
    { key: 'CV', label: 'CV' },
    { key: 'LETTRE_MOTIVATION', label: 'Lettre de motivation' },
    { key: 'CNI', label: "Carte d'identité" },
    { key: 'DIPLOME', label: 'Diplôme' },
  ];

  constructor(
    private fb: FormBuilder,
    private formulaireService: FormulaireService,
    private documentService: DocumentService,
    private cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    this.formulaireForm = this.fb.group({

      genre: [''],
      telephone: [null, [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      nationalite: [''],
      adresse: [''],

      dernierDiplome: [''],
      etablissement: [''],
      anneeObtention: [null],

      programmeChoisi: [''],
      campusVille: [''],
      anneeIntegration: [null],
      majeur: [''],
    });

    for (const docType of this.documentTypes) {
      this.selectedFiles[docType.key] = null;
    }

    this.formulaireForm.get('anneeIntegration')?.valueChanges.subscribe(() => this.updateMajeurValidators());
    this.updateMajeurValidators();

    try {
      const existing = await this.formulaireService.getMyFormulaire();
      this.isExisting = true;
      this.formulaireId = existing.id;
      this.formulaireForm.patchValue(existing);
      this.updateMajeurValidators();
      await this.loadDocuments();
      this.cdr.detectChanges();
    } catch (error: any) {
      if (error.status !== 404) {
        console.error(error);
      }
    }
  }

  get isMajeurRequired(): boolean {
    const annee = Number(this.formulaireForm.get('anneeIntegration')?.value);
    return annee === 4 || annee === 5;
  }

  private updateMajeurValidators(): void {
    const majeurControl = this.formulaireForm.get('majeur');
    if (!majeurControl) {
      return;
    }
    if (this.isMajeurRequired) {
      majeurControl.enable({ emitEvent: false });
      majeurControl.setValidators([Validators.required]);
    } else {
      majeurControl.setValue('', { emitEvent: false });
      majeurControl.disable({ emitEvent: false });
      majeurControl.clearValidators();
    }
    majeurControl.updateValueAndValidity({ emitEvent: false });
  }

  private async loadDocuments(): Promise<void> {
    if (!this.formulaireId) {
      return;
    }
    try {
      const documents = await this.documentService.getDocuments(this.formulaireId);
      this.uploadedDocuments = new Map(documents.map(d => [d.documentType, d]));
    } catch (error) {
      console.error(error);
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

  onFileSelected(event: Event, documentType: string): void {
    const input = event.target as HTMLInputElement;
    this.selectedFiles[documentType] = input.files?.[0] ?? null;
  }

  isDocumentProvided(documentType: string): boolean {
    return this.uploadedDocuments.has(documentType) || !!this.selectedFiles[documentType];
  }

  getDisplayFileName(documentType: string): string {
    const selected = this.selectedFiles[documentType];
    if (selected) {
      return selected.name;
    }
    const uploaded = this.uploadedDocuments.get(documentType);
    if (uploaded) {
      return uploaded.fileName;
    }
    return 'Aucun fichier choisi';
  }

  private async uploadPendingDocuments(): Promise<void> {
    if (!this.formulaireId) {
      return;
    }

    const uploads = this.documentTypes
      .map(docType => this.selectedFiles[docType.key])
      .map((file, index) => file ? this.documentService.upload(this.formulaireId!, this.documentTypes[index].key, file) : null)
      .filter((upload): upload is Promise<DocumentFormulaire> => upload !== null);

    if (uploads.length > 0) {
      await Promise.all(uploads);
      for (const docType of this.documentTypes) {
        this.selectedFiles[docType.key] = null;
      }
      await this.loadDocuments();
    }
  }

  async onSubmit(): Promise<void> {
    const formulaire: Formulaire = this.formulaireForm.value;

    try {
      if (this.isExisting) {
        await this.formulaireService.update(formulaire);
      } else {
        const created = await this.formulaireService.create(formulaire);
        this.isExisting = true;
        this.formulaireId = created.id;
      }

      await this.uploadPendingDocuments();

      this.successMessage = 'Dossier sauvegardé avec succès !';
      this.errorMessage = '';
      this.cdr.detectChanges();
    } catch (error) {
      this.errorMessage = 'Une erreur est survenue lors de la sauvegarde';
      this.successMessage = '';
      console.error(error);
    }
  }

  private isFieldFilled(field: string): boolean {
    const control = this.formulaireForm.get(field);
    if (!control) {
      return false;
    }
    if (control.disabled) {
      return true;
    }
    return !!control.value && control.valid;
  }

  get completionStep1(): number {
    const fields = ['genre', 'telephone', 'nationalite', 'adresse'];
    const filled = fields.filter(f => this.isFieldFilled(f)).length;
    return Math.round((filled / fields.length) * 100);
  }

  get completionStep2(): number {
    const fields = ['dernierDiplome', 'etablissement', 'anneeObtention'];
    const filled = fields.filter(f => this.formulaireForm.get(f)?.value).length;
    return Math.round((filled / fields.length) * 100);
  }

  get completionStep3(): number {
    const fields = ['programmeChoisi', 'campusVille', 'anneeIntegration', 'majeur'];
    const filled = fields.filter(f => this.isFieldFilled(f)).length;
    return Math.round((filled / fields.length) * 100);
  }

  get completionStep4(): number {
    const filled = this.documentTypes.filter(d => this.isDocumentProvided(d.key)).length;
    return Math.round((filled / this.documentTypes.length) * 100);
  }

  get completionTotal(): number {
    return Math.round((this.completionStep1 + this.completionStep2 + this.completionStep3 + this.completionStep4) / 4);
  }

  get isFormComplete(): boolean {
    const fields = [
      'genre', 'telephone', 'nationalite', 'adresse',
      'dernierDiplome', 'etablissement', 'anneeObtention',
      'programmeChoisi', 'campusVille', 'anneeIntegration', 'majeur'
    ];
    return fields.every(f => this.isFieldFilled(f))
      && this.documentTypes.every(d => this.isDocumentProvided(d.key));
  }
}
