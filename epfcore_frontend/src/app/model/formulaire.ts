export interface Formulaire {
  id?: number;
  dernierDiplome?: string;
  etablissement?: string;
  niveauEtude?: string;
  anneeObtention?: number | null;
  programmeChoisi?: string;
  motivations?: string;
  campusVille?: string;
  soumis?: boolean;
  genre?: string;
  telephone?: string;
  nationalite?: string;
  adresse?: string;
  anneeIntegration?: number | null;
  majeur?: string;
}
