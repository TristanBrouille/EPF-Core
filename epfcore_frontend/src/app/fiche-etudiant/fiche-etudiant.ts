import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

export type Sexe = 'M' | 'F' | 'Autre';
export type Boursier = 'OUI' | 'NON';

export interface Etudiant {
  id: number;
  num_etudiant: string;
  prenom: string;
  nom: string;
  email: string;
  sexe: Sexe;
  nationalite: string;
  bourse: Boursier;
  adresse: string;
  telephone: string;
  programme: string;
  annee_academique: string;
  formation: string;
  campus: string;
  date_inscription: string;
  dernier_diplome: string;
  lien_photo?: string;
}

@Component({
  selector: 'app-fiche-etudiant',
  imports: [CommonModule],
  templateUrl: './fiche-etudiant.html',
  styleUrl: './fiche-etudiant.scss',
})
export class FicheEtudiant {

    etudiant: Etudiant = {
    id: 1,
    num_etudiant: 'EPF-2021-4872',
    prenom: 'Marie',
    nom: 'Dupont',
    email: 'marie.dupont@epf.fr',
    sexe: 'F',
    nationalite: 'Française',
    bourse: 'NON',
    adresse: '12 Rue des Lilas, 75014 Paris',
    telephone: '+33 6 12 34 56 78',
    programme: 'majeur système numérique & intelligent',
    annee_academique: '4A',
    formation: 'FGE',
    campus: 'Cachan',
    date_inscription: '2021-09-06',
    dernier_diplome: 'Baccalauréat Scientifique',
    lien_photo: ''
  };
 
  get initialesAvatar(): string {
    return `${this.etudiant.prenom[0]}${this.etudiant.nom[0]}`.toUpperCase();
  }
 
  get dateInscriptionFormatee(): string {
    return new Date(this.etudiant.date_inscription).toLocaleDateString('fr-FR', {
      day: '2-digit', month: 'long', year: 'numeric'
    });
  }

}
