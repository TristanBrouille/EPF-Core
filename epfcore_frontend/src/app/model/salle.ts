export interface Campus {
  id?: number;
  ville: string;
  adresse?: string;
  codePostal?: number;
}
export interface Salle {
  id?: number;
  nomSalle: string;
  capacite: number;
  equipement: string;
  campus: number | null;
  typeSalle: string;
}