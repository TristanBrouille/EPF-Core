import { Salle } from './salle';

export interface Reservation {
  idReservation?: number;
  nomReserveur: string;
  date: any;
  heureDebut: any;
  heureFin: any;
  nbrPersonnes: number;
  status: 'EN_ATTENTE' | 'ACCEPTE' | 'REFUSE';
  salle?: any;
}