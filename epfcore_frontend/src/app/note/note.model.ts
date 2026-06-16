// ─────────────────────────────────────────────────────────────────────────────
//  note.model.ts  –  Interfaces TypeScript pour le module Notes (Module 5)
// ─────────────────────────────────────────────────────────────────────────────

export interface CarnetDeNotes {
  id: number;
  intitule: string;
  anneeAcademique: string;
  statut: 'BROUILLON' | 'PUBLIE';
  moyenneClasse: number | null;
  dateCreation: string | null;
  datePublication: string | null;
  uniteEnseignementId?: number;
  uniteEnseignementIntitule?: string;
  nbEvaluations: number;
}

export interface Evaluation {
  error: any;
  id: number;
  intitule: string;
  type: 'DS' | 'TP' | 'PROJET' | 'EXAMEN' | 'RATTRAPAGE' | 'AUTRE';
  coef: number;
  noteMax: number;
  dateEval: string | null;
  carnetId: number;
}

export interface Note {
  id: number;
  valeurNote: number | null;
  absent: boolean;
  commentaire: string | null;
  source: 'MANUELLE' | 'IMPORT_CSV';
  dateSaisie: string;
  etudiantId: number;
  evaluationId: number;
}

export interface NoteInput {
  evaluationId: number;
  etudiantId: number;
  valeur: number | null;
  commentaire: string;
}

export interface MoyenneRow {
  etudiantId: number;
  numero: string;
  nomComplet: string;
  moyenne: number | null;
}

export interface ImportResult {
  imported: number;
  errors: number;
  errorDetails: string[];
}

export interface Etudiant {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  numero: string;
  programme: string | null;
  anneeAcademique: string | null;
}

// Ligne du tableau de saisie (carnet ouvert)
export interface LigneNote {
  etudiant: Etudiant;
  notes: { [evaluationId: number]: number | null };
  absent: { [evaluationId: number]: boolean };
  moyenne: number | null;
}