export interface Formulaire {
  id?: number;
  dernierDiplome: string;
  etablissement: string;
  niveauEtude: string;
  anneeObtention: number | null;
  programmeChoisi: string;
  campusChoisi: string;
  motivations: string;
  avancement?: string;
}
