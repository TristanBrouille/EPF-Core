// import { Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { FormsModule } from '@angular/forms';
// import { RouterLink } from '@angular/router';
// import { NoteService } from './note.service';
// import {
//   CarnetDeNotes, Evaluation, MoyenneRow, ImportResult
// } from './note.model';
// import { HttpErrorResponse } from '@angular/common/http';

import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { RouterLink } from "@angular/router";
import { CarnetDeNotes, Evaluation, ImportResult, MoyenneRow } from "../note/note.model";
import { NoteService } from "../note/note.service";
import { HttpErrorResponse } from "@angular/common/http";



type View = 'list' | 'carnet' | 'create' | 'import';

@Component({
  selector: 'app-carnet-notes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './carnetNotes.html',
  styleUrls: ['./carnetNotes.scss']
})
export class CarnetNotesComponent implements OnInit {

  // ── Navigation ────────────────────────────────────────────────────────────
  currentView: View = 'list';

  // ── État liste ────────────────────────────────────────────────────────────
  carnets: CarnetDeNotes[]  = [];
  loading = false;
  errorMsg = '';
  successMsg = '';

  // ── Création carnet ───────────────────────────────────────────────────────
  newCarnet = { intitule: '', anneeAcademique: '2024-2025', uniteEnseignementId: null as number | null };

  // ── Vue carnet ouvert ─────────────────────────────────────────────────────
  selectedCarnet: CarnetDeNotes | null = null;
  evaluations: Evaluation[] = [];
  moyennes: MoyenneRow[] = [];

  // Saisie de notes : structure [etudiantId][evaluationId] = valeur
  notesGrid: { [key: string]: number | '' } = {};
  absentGrid: { [key: string]: boolean } = {};
  etudiantsAffichage: { id: number; numero: string; nomComplet: string }[] = [];

  // Ajout évaluation
  newEval = { intitule: '', type: 'DS', coef: 1, noteMax: 20 };
  showAddEval = false;

  // ── Import CSV ────────────────────────────────────────────────────────────
  importEvalId: number | null = null;
  importFile: File | null = null;
  importResult: ImportResult | null = null;
  importLoading = false;

  readonly TYPES_EVAL = ['DS', 'TP', 'PROJET', 'EXAMEN', 'RATTRAPAGE', 'AUTRE'];

  constructor(private noteService: NoteService) {}

  ngOnInit(): void {
    this.loadCarnets();
  }

  // ── Liste des carnets ──────────────────────────────────────────────────────
  // loadCarnets(): void {
  //   this.loading = true;
  //   this.noteService.getAllCarnets().subscribe({
  //     next: (data: CarnetDeNotes[]) => { this.carnets = data; this.loading = false; },
  //     error: (e: ImportResult)   => { this.errorMsg = 'Erreur chargement carnets'; this.loading = false; }
  //   });
  // }

  // loadCarnets(): void {
  //   this.loading = false;

  //   this.noteService.getAllCarnets().subscribe({
  //     next: (data) => {
  //       console.log('✔ Carnets reçus :', data);
  //       this.carnets = data;
  //       this.loading = false;
  //     },
  //     error: (err) => {
  //       console.error('❌ Erreur API getAllCarnets :', err);
  //       this.errorMsg = 'Erreur chargement carnets';
  //       this.loading = false;
  //     }
  //   });
  // }

  loadCarnets(): void {
    console.log('LOAD CARNETS APPELÉ'); ///////////////////////////////////////////////////////
    this.loading = true;
    ///////////////
    console.log('START LOAD');

    this.loading = true;

    this.noteService.getAllCarnets().subscribe({
      next: data => {
        console.log('NEXT');

        this.carnets = [...data];

        console.log('BEFORE FALSE');

        this.loading = false;

        console.log('AFTER FALSE');
      },
      error: err => {
        console.log('ERROR', err);

        this.loading = false;
      }
    });
    //////////////////
    // this.noteService.getAllCarnets().subscribe({
    //   next: data => {
    //     console.log('AVANT affectation', this.carnets);
    //     console.log('DATA reçue', data);

    //     this.carnets = [...data];

    //     console.log('APRÈS affectation', this.carnets);

    //     this.loading = false;
    //   }
    // });
    // this.noteService.getAllCarnets().subscribe({
    //   next: data => {
    //     console.log('Carnets reçus :', data);      // ← ouvrez F12 Console
    //     console.log('Nombre :', data.length);
    //     //this.carnets = data;
    //     this.carnets = [...data];
    //     this.loading = false;
    //   },
    //   error: e => {
    //     console.error('Erreur complète :', e);
    //     this.errorMsg = 'Erreur : ' + e.status + ' ' + e.message;
    //     this.loading = false;
    //   }
    // });
  }


  // ── Créer un carnet ────────────────────────────────────────────────────────
  showCreate(): void   { this.currentView = 'create'; this.clearMessages(); }
  cancelCreate(): void { this.currentView = 'list'; }

  submitCarnet(): void {
    if (!this.newCarnet.intitule.trim()) {
      this.errorMsg = 'L\'intitulé est obligatoire.';
      return;
    }
    this.loading = true;
    this.noteService.creerCarnet(this.newCarnet).subscribe({
      next: (carnet: CarnetDeNotes) => {
        this.carnets.unshift(carnet);
        this.successMsg = `Carnet "${carnet.intitule}" créé avec succès.`;
        this.newCarnet  = { intitule: '', anneeAcademique: '2024-2025', uniteEnseignementId: null };
        this.currentView = 'list';
        this.loading = false;
      },
      error: (e: HttpErrorResponse) => { this.errorMsg = 'Erreur création : ' + (e.error?.error || e.message); this.loading = false; }
    });
  }

  // ── Ouvrir un carnet (saisie des notes) ───────────────────────────────────
  ouvrirCarnet(carnet: CarnetDeNotes): void {
    this.selectedCarnet = carnet;
    this.currentView    = 'carnet';
    this.clearMessages();
    this.loadEvaluations(carnet.id);
    this.loadMoyennes(carnet.id);
  }

  // backToList(): void {
  //   this.selectedCarnet = null;
  //   this.currentView    = 'list';
  //   this.loadCarnets();
  // }
  backToList(): void {

    this.selectedCarnet = null;

    this.currentView = 'list';

    }

  // loadEvaluations(carnetId: number): void {
  //   this.noteService.getEvaluations(carnetId).subscribe({
  //     next: (evals: Evaluation[]) => { this.evaluations = evals; },
  //     error: ()   => { this.errorMsg = 'Erreur chargement évaluations'; }
  //   });
  // }

  // loadMoyennes(carnetId: number): void {
  //   this.noteService.getMoyennes(carnetId).subscribe({
  //     next: (data: MoyenneRow[]) => {
  //       this.moyennes = data;
  //       // Initialiser la grille depuis les moyennes (liste des étudiants)
  //       this.etudiantsAffichage = data.map(r => ({
  //         id: r.etudiantId, numero: r.numero, nomComplet: r.nomComplet
  //       }));
  //     },
  //     error: () => {}
  //   });
  // }

  loadEvaluations(carnetId: number): void {
    this.noteService.getEvaluations(carnetId).subscribe({
      next: (evals) => {
        console.log('EVALUATIONS', evals);
        this.evaluations = evals;
      }
    });
  }

  loadMoyennes(carnetId: number): void {
    this.noteService.getMoyennes(carnetId).subscribe({
      next: (data) => {
        console.log('MOYENNES', data);
        this.moyennes = data;
        this.etudiantsAffichage = data.map(r => ({
          id: r.etudiantId, numero: r.numero, nomComplet: r.nomComplet
        }));
      }
    });
  }

  // ── Saisie manuelle ────────────────────────────────────────────────────────
  gridKey(etudiantId: number, evalId: number): string {
    return `${etudiantId}_${evalId}`;
  }

  saisirNote(etudiantId: number, evalId: number): void {
    if (!this.selectedCarnet) return;
    const key    = this.gridKey(etudiantId, evalId);
    const absent = this.absentGrid[key] ?? false;
    const valeur = absent ? null : (this.notesGrid[key] !== '' ? Number(this.notesGrid[key]) : null);

    this.noteService.saisirNote({ evaluationId: evalId, etudiantId, valeur, commentaire: '' }).subscribe({
      next: () => {
        this.successMsg = 'Note enregistrée.';
        setTimeout(() => this.successMsg = '', 2500);
        this.loadMoyennes(this.selectedCarnet!.id);
      },
      error: (e: HttpErrorResponse) => { this.errorMsg = e.error?.error || 'Erreur enregistrement'; }
    });
  }

  toggleAbsent(etudiantId: number, evalId: number): void {
    const key = this.gridKey(etudiantId, evalId);
    this.absentGrid[key] = !this.absentGrid[key];
    if (this.absentGrid[key]) {
      this.notesGrid[key] = '';
      this.saisirNote(etudiantId, evalId);
    }
  }

  // ── Publier le carnet ──────────────────────────────────────────────────────
  publier(): void {
    if (!this.selectedCarnet) return;
    if (!confirm('Publier ce carnet ? Les notes ne pourront plus être modifiées.')) return;

    this.noteService.publierCarnet(this.selectedCarnet.id).subscribe({
      next: (c: CarnetDeNotes) => {
        this.selectedCarnet = c;
        this.successMsg = 'Carnet publié avec succès.';
      },
      error: (e: HttpErrorResponse) => { this.errorMsg = e.error?.error || 'Erreur publication'; }
    });
  }

  // ── Ajouter une évaluation ─────────────────────────────────────────────────
  submitEval(): void {
    if (!this.selectedCarnet || !this.newEval.intitule.trim()) return;

    this.noteService.ajouterEvaluation(this.selectedCarnet.id, this.newEval).subscribe({
      next: (e: Evaluation) => {
        this.evaluations.push(e);
        this.showAddEval = false;
        this.newEval     = { intitule: '', type: 'DS', coef: 1, noteMax: 20 };
        this.successMsg  = `Évaluation "${e.intitule}" ajoutée.`;
      },
      error: (e: Evaluation) => { this.errorMsg = e.error?.error || 'Erreur ajout évaluation'; }
    });
  }

  // ── Import CSV ────────────────────────────────────────────────────────────
  showImport(): void   { this.currentView = 'import'; this.importResult = null; this.clearMessages(); }
  cancelImport(): void { this.currentView = 'carnet'; }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.importFile = input.files?.[0] ?? null;
  }

  submitImport(): void {
    if (!this.importEvalId || !this.importFile) {
      this.errorMsg = 'Sélectionnez une évaluation et un fichier CSV.';
      return;
    }
    this.importLoading = true;
    this.noteService.importerCSV(this.importEvalId, this.importFile).subscribe({
      next: (result: ImportResult) => {
        this.importResult  = result;
        this.importLoading = false;
        this.successMsg    = `${result.imported} note(s) importée(s).`;
        if (this.selectedCarnet) this.loadMoyennes(this.selectedCarnet.id);
      },
      error: (e: HttpErrorResponse) => {
        this.errorMsg     = e.error?.error || 'Erreur import';
        this.importLoading = false;
      }
    });
  }

  // ── Utilitaires ───────────────────────────────────────────────────────────
  clearMessages(): void { this.errorMsg = ''; this.successMsg = ''; }

  getMoyenne(etudiantId: number): number | null {
    return this.moyennes.find(m => m.etudiantId === etudiantId)?.moyenne ?? null;
  }

  formatNote(val: number | null): string {
    if (val === null || val === undefined) return '—';
    return val.toFixed(2);
  }

  getGrade(moy: number | null): string {
    if (moy === null) return '—';
    if (moy >= 16) return 'A';
    if (moy >= 14) return 'B';
    if (moy >= 12) return 'C';
    if (moy >= 10) return 'D';
    if (moy >=  8) return 'E';
    if (moy >=  4) return 'FX';
    return 'F';
  }
}