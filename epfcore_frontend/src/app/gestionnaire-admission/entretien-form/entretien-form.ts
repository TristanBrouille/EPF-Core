import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EntretienService } from '../entretien-service';
import { FormulaireService } from '../../formulaire-inscription/formulaireService';
import { Formulaire } from '../../model/formulaire';
import { Interviewer } from '../../model/entretien';

@Component({
  selector: 'app-entretien-form',
  imports: [ReactiveFormsModule],
  templateUrl: './entretien-form.html',
  styleUrl: './entretien-form.scss',
})
export class EntretienForm implements OnInit {
  entretienForm!: FormGroup;
  formulaires: Formulaire[] = [];
  interviewers: Interviewer[] = [];
  errorMessage = '';
  candidatSearch = '';
  interviewerSearch = '';
  readonly timeSlots = EntretienForm.generateTimeSlots();

  constructor(
    private readonly fb: FormBuilder,
    private readonly entretienService: EntretienService,
    private readonly formulaireService: FormulaireService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    this.entretienForm = this.fb.group({
      formulaireId: [null, Validators.required],
      interviewerId: [null, Validators.required],
      dateEntretien: ['', [Validators.required, EntretienForm.notSundayValidator]],
      heureEntretien: ['', Validators.required],
      typeEntretien: ['PRESENTIEL', Validators.required],
      campusVille: [''],
      salle: [''],
      lienVisio: [''],
    });

    try {
      const [formulaires, interviewers, entretiens] = await Promise.all([
        this.formulaireService.getAllSoumis(),
        this.entretienService.getInterviewers(),
        this.entretienService.getAll(),
      ]);
      const formulaireIdsAvecEntretien = new Set(
        entretiens
          .filter(entretien => entretien.statut === 'PLANIFIE' || entretien.statut === 'REALISE')
          .map(entretien => entretien.formulaireId)
      );
      this.formulaires = formulaires.filter(formulaire => formulaire.id !== undefined && !formulaireIdsAvecEntretien.has(formulaire.id));
      this.interviewers = interviewers;

      const formulaireId = Number(this.route.snapshot.queryParamMap.get('formulaireId'));
      if (formulaireId) {
        if (!this.formulaires.some(formulaire => formulaire.id === formulaireId)) {
          const preselected = formulaires.find(formulaire => formulaire.id === formulaireId);
          if (preselected) {
            this.formulaires = [...this.formulaires, preselected];
          }
        }
        this.entretienForm.patchValue({ formulaireId });
      }
    } catch (error) {
      this.errorMessage = 'Impossible de charger les données nécessaires';
      console.error(error);
    }
    this.cdr.detectChanges();
  }

  get isVisio(): boolean {
    return this.entretienForm.get('typeEntretien')?.value === 'VISIO';
  }

  get isPresentiel(): boolean {
    return this.entretienForm.get('typeEntretien')?.value === 'PRESENTIEL';
  }

  get isFormReady(): boolean {
    if (this.entretienForm.invalid) {
      return false;
    }
    const value = this.entretienForm.value;
    if (!value.formulaireId || !value.interviewerId) {
      return false;
    }
    if (this.isVisio) {
      return !!value.lienVisio;
    }
    if (this.isPresentiel) {
      return !!value.campusVille && !!value.salle;
    }
    return true;
  }

  get filteredFormulaires(): Formulaire[] {
    const term = this.candidatSearch.trim().toLowerCase();
    if (!term) {
      return this.formulaires;
    }
    return this.formulaires.filter(formulaire => {
      const text = `${formulaire.user?.firstname ?? ''} ${formulaire.user?.lastname ?? ''} ${formulaire.user?.email ?? ''}`;
      return text.toLowerCase().includes(term);
    });
  }

  get selectedCandidatCampus(): string | undefined {
    const formulaireId = this.entretienForm.get('formulaireId')?.value;
    if (!formulaireId) {
      return undefined;
    }
    return this.formulaires.find(formulaire => formulaire.id === formulaireId)?.campusVille;
  }

  campusLabel(ville: string): string {
    return this.selectedCandidatCampus === ville ? `${ville} (campus choisi par le candidat)` : ville;
  }

  get filteredInterviewers(): Interviewer[] {
    const term = this.interviewerSearch.trim().toLowerCase();
    if (!term) {
      return this.interviewers;
    }
    return this.interviewers.filter(interviewer => {
      const text = `${interviewer.firstname} ${interviewer.lastname} ${interviewer.email}`;
      return text.toLowerCase().includes(term);
    });
  }

  onCandidatSearch(value: string): void {
    this.candidatSearch = value;
  }

  onInterviewerSearch(value: string): void {
    this.interviewerSearch = value;
  }

  async onSubmit(): Promise<void> {
    if (!this.isFormReady) {
      return;
    }
    try {
      const { dateEntretien, heureEntretien, ...rest } = this.entretienForm.value;
      const entretien = { ...rest, dateHeure: `${dateEntretien}T${heureEntretien}:00` };
      if (!this.isPresentiel) {
        entretien.campusVille = '';
        entretien.salle = '';
      }
      if (!this.isVisio) {
        entretien.lienVisio = '';
      }
      const created = await this.entretienService.create(entretien);
      await this.router.navigate(['/admin/entretiens', created.id]);
    } catch (error) {
      this.errorMessage = "Impossible de créer l'entretien";
      console.error(error);
      this.cdr.detectChanges();
    }
  }

  goBack(): void {
    this.router.navigate(['/admin/entretiens']);
  }

  private static generateTimeSlots(): string[] {
    const slots: string[] = [];
    for (let hour = 8; hour <= 19; hour++) {
      slots.push(`${hour.toString().padStart(2, '0')}:00`);
      slots.push(`${hour.toString().padStart(2, '0')}:30`);
    }
    return slots;
  }

  private static notSundayValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
      return null;
    }
    const [year, month, day] = control.value.split('-').map(Number);
    const date = new Date(year, month - 1, day);
    return date.getDay() === 0 ? { sunday: true } : null;
  }
}
