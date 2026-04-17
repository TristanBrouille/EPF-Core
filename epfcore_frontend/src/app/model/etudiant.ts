export interface Etudiant {
  id: number;
  numEtudiant: string;   
  user: User;            
  sexe: string;
  nationalite: string;
  bourse: string;
  adresse: string;
  telephone: string;
  programme: string;
  anneeAcademique: string;
  formation: string;
  campus: string;
  dateInscription: string;
  dernierDiplome: string;
  lienPhoto?: string;
}

export interface User {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
  birthday: Date;
}

