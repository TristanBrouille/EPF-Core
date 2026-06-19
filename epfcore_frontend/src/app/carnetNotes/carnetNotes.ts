import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { RouterLink } from "@angular/router";
import { CarnetDeNotes, Evaluation, ImportResult, MoyenneRow, Note } from "../note/note.model";
import { NoteService } from "../note/note.service";
import { HttpErrorResponse } from "@angular/common/http";
import { Observable } from "rxjs";

type View = 'list' | 'carnet' | 'create' | 'import';

@Component({
  selector: 'app-carnet-notes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './carnetNotes.html',
  styleUrls: ['./carnetNotes.scss']
})
export class CarnetNotesComponent implements OnInit {

  // Navigation 
  currentView: View = 'list';

  //  État liste 
  carnets: CarnetDeNotes[]  = [];
  loading = false;
  errorMsg = '';
  successMsg = '';

  //  Création carnet 
  newCarnet = { intitule: '', anneeAcademique: '2024-2025', uniteEnseignementId: null as number | null };

  //  Vue carnet ouvert 
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

  // Import CSV 
  importEvalId: number | null = null;
  importFile: File | null = null;
  importResult: ImportResult | null = null;
  importLoading = false;

  showAddNote = false;
  newNote: {
    etudiantId: number | null;
    evaluationId: number | null;
    valeur: number | null;
    absent: boolean;
    commentaire: string;
  } = { etudiantId: null, evaluationId: null, valeur: null, absent: false, commentaire: '' };

  readonly TYPES_EVAL = ['DS', 'TP', 'PROJET', 'EXAMEN', 'RATTRAPAGE', 'AUTRE'];
  http: any;
  API: any;

  constructor(private noteService: NoteService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadCarnets();
  }

  loadCarnets(): void {
    console.log('LOAD CARNETS APPELÉ');

    this.loading = true;

    this.noteService.getAllCarnets().subscribe({
      next: data => {

        this.carnets = [...data];

        this.loading = false;

        this.cdr.detectChanges();
      },
      error: err => {

        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  
  }


  // Créer un carnet 
  showCreate(): void   { this.currentView = 'create'; this.clearMessages(); }
  cancelCreate(): void { this.currentView = 'list'; }

  submitCarnet(): void {
    if (!this.newCarnet.intitule.trim()) {
      this.errorMsg = 'L\'intitulé est obligatoire.';
      return;
    }
    this.loading = true;
    this.noteService.creerCarnet(this.newCarnet).subscribe({
      next: carnet => {
        this.carnets.unshift(carnet);
        this.newCarnet = { intitule: '', anneeAcademique: '2024-2025', uniteEnseignementId: null };
        this.loading = false;
        this.ouvrirCarnet(carnet);
      },
      error: e => {
        this.errorMsg = 'Erreur création : ' + (e.error?.error || e.message);
        this.loading = false;
      }
    });
  }

  backToList(): void {
    this.selectedCarnet = null;
    this.currentView = 'list';
    }

  loadEvaluations(carnetId: number): void {
    this.noteService.getEvaluations(carnetId).subscribe({
      next: (evals) => {
        console.log('EVALUATIONS', evals);
        this.evaluations = evals;
        this.cdr.detectChanges();
      }
    });
  }

  loadMoyennes(carnetId: number): void {
    this.noteService.getMoyennes(carnetId).subscribe({
      next: (data) => {
        this.moyennes = data;
        this.etudiantsAffichage = data.map(r => ({
          id: r.etudiantId, numero: r.numero, nomComplet: r.nomComplet
        }));
        this.cdr.detectChanges();
      }
    });
  }

  // Saisie manuelle
  gridKey(etudiantId: number, evalId: number): string {
    return `${etudiantId}_${evalId}`;
  }

  toggleAbsent(etudiantId: number, evalId: number): void {
    const key = this.gridKey(etudiantId, evalId);
    this.absentGrid[key] = !this.absentGrid[key];
    if (this.absentGrid[key]) {
      this.notesGrid[key] = '';
      this.saisirNote(etudiantId, evalId);
    }
  }

  // Publier le carnet
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

  // Ajouter une évaluation 
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

  onAbsentChange(): void {
    if (this.newNote.absent) {
      this.newNote.valeur = null;
    }
  }

  submitNote(): void {
    if (!this.newNote.etudiantId || !this.newNote.evaluationId) {
      this.errorMsg = 'L\'étudiant et l\'évaluation sont obligatoires.';
      return;
    }

    if (!this.newNote.absent && this.newNote.valeur === null) {
      this.errorMsg = 'Saisissez une note ou cochez Absent.';
      return;
    }

    const eval_ = this.evaluations.find(e => e.id === this.newNote.evaluationId);
    if (eval_ && !this.newNote.absent &&
        this.newNote.valeur !== null &&
        (this.newNote.valeur < 0 || this.newNote.valeur > eval_.noteMax)) {
      this.errorMsg = `La note doit être entre 0 et ${eval_.noteMax}.`;
      return;
    }

    this.noteService.saisirNote({
      evaluationId: this.newNote.evaluationId!,
      etudiantId:   this.newNote.etudiantId!,
      valeur:       this.newNote.absent ? null : this.newNote.valeur,
      commentaire:  this.newNote.commentaire
    }).subscribe({
      next: () => {
        this.successMsg = 'Note enregistrée.';
        setTimeout(() => this.successMsg = '', 2500);
        this.showAddNote = false;
        this.newNote = { etudiantId: null, evaluationId: null, valeur: null, absent: false, commentaire: '' };
        this.loadMoyennes(this.selectedCarnet!.id);
        this.loadNotesExistantes(this.selectedCarnet!.id);
      },
      error: (e) => {
        this.errorMsg = e.error?.error || 'Erreur enregistrement note';
      }
    });
  }

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

  ouvrirCarnet(carnet: CarnetDeNotes): void {
    this.selectedCarnet = carnet;
    this.currentView    = 'carnet';
    this.notesGrid      = {};
    this.absentGrid     = {};
    this.clearMessages();
    this.loadEvaluations(carnet.id);
    this.loadMoyennes(carnet.id);
    this.loadNotesExistantes(carnet.id);
  }

  loadNotesExistantes(carnetId: number): void {
    this.noteService.getNotesByCarnet(carnetId).subscribe({
      next: notes => {
        console.log('NOTES REÇUES DU SERVEUR :', notes);
        notes.forEach((n: any) => {
          const etudiantId = n.etudiantId || n.idEtudiant || n.etudiant?.id;
          const evaluationId = n.evaluationId || n.idEvaluation || n.evaluation?.id;

          const valeurNote = n.valeurNote !== undefined ? n.valeurNote : (n.valeur !== undefined ? n.valeur : n.note);

          if (etudiantId && evaluationId) {
            const key = this.gridKey(etudiantId, evaluationId);
            this.notesGrid[key]  = (valeurNote !== null && valeurNote !== undefined) ? valeurNote : '';
            this.absentGrid[key] = n.absent || false;
          }
        });
        this.cdr.detectChanges();
      }
    });
  }

  saisirNote(etudiantId: number, evalId: number): void {
    if (!this.selectedCarnet) return;
    
    const key = this.gridKey(etudiantId, evalId);
    const absent = this.absentGrid[key] ?? false;

    const saisie = this.notesGrid[key];
    const valeur = absent ? null : (saisie !== '' && saisie !== null && saisie !== undefined ? Number(saisie) : null);

    const payload = { 
      evaluationId: evalId, 
      etudiantId: etudiantId, 
      valeur: valeur, 
      commentaire: '' 
    };

    this.noteService.saisirNote(payload).subscribe({
      next: (response) => {
        this.successMsg = 'Note enregistrée avec succès.';
        setTimeout(() => this.successMsg = '', 2500);
        this.notesGrid[key] = valeur !== null ? valeur : '';
        this.loadMoyennes(this.selectedCarnet!.id);
        this.cdr.detectChanges();
      },
      error: (e: HttpErrorResponse) => { 
        this.errorMsg = e.error?.error || 'Erreur lors de l\'enregistrement de la note'; 
      }
    });
  }

}
