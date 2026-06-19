import { PersonExpose } from './person-expose';
import { DecisionAdmission } from './formulaire';

export type TypeEntretien = 'PRESENTIEL' | 'VISIO' | 'TELEPHONE';
export type StatutEntretien = 'PLANIFIE' | 'REALISE' | 'ANNULE' | 'NO_SHOW';

export interface Interviewer {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
}

export interface EntretienCandidature {
  id?: number;
  formulaireId: number;
  candidat?: PersonExpose;
  interviewerId?: number | null;
  interviewer?: PersonExpose | null;
  dateHeure: string;
  campusVille?: string;
  salle?: string;
  lienVisio?: string;
  typeEntretien: TypeEntretien;
  statut?: StatutEntretien;
  note?: number | null;
  commentaire?: string;
  dateCreation?: string;
  decisionAdmission?: DecisionAdmission;
}
