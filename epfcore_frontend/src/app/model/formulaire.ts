import { PersonExpose } from './person-expose';

export interface Formulaire {
  id?: number;
  user?: PersonExpose;
  dateSoumission?: string;
  dernierDiplome?: string;
  etablissement?: string;
  niveauEtude?: string;
  anneeObtention?: number | null;
  programmeChoisi?: string;
  campusVille?: string;
  soumis?: boolean;
  genre?: string;
  telephone?: string;
  nationalite?: string;
  adresse?: string;
  anneeIntegration?: number | null;
  majeur?: string;
}
