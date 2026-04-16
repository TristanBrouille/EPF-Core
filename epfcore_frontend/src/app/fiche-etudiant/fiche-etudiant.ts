import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';

export interface User {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
}

export interface Etudiant {
  id: number;
  numEtudiant: string;   // camelCase comme Java
  user: User;            // objet imbriqué
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

@Component({
  selector: 'app-fiche-etudiant',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './fiche-etudiant.html',
  styleUrls: ['./fiche-etudiant.scss'],
})
export class FicheEtudiant implements OnInit {

 etudiant: Etudiant | null = null;

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.http.post('http://localhost:8080/login', {
      email: 'jean.dupont@example.com',
      password: 'password123'
    }, { withCredentials: true }).subscribe({
      next: () => this.loadEtudiant(),
      error: (err) => console.error('Erreur login', err)
    });
  }

  loadEtudiant(): void {
    this.http.get<Etudiant>('http://localhost:8080/api/etudiants/1', {
      withCredentials: true
    }).subscribe({
      next: (data) => {
        this.etudiant = data;
        this.cdr.detectChanges(); // force Angular à re-render
      },
      error: (err) => console.error('Erreur chargement étudiant', err)
    });
  }

  get initialesAvatar(): string {
    if (!this.etudiant) return '';
    return `${this.etudiant.user.firstname[0]}${this.etudiant.user.lastname[0]}`.toUpperCase();
  }

  get dateInscriptionFormatee(): string {
    if (!this.etudiant) return '';
    return new Date(this.etudiant.dateInscription).toLocaleDateString('fr-FR', {
      day: '2-digit', month: 'long', year: 'numeric'
    });
  }

  get telephoneFormate(): string {
  if (!this.etudiant) return '';
  return this.etudiant.telephone.replace(/(\d{2})(?=\d)/g, '$1 ');
}

//   downloadPDF() {
//   const payload = this.etudiant;

//   this.http.post('http://localhost:3000/pdf', payload, {
//     responseType: 'blob'
//   }).subscribe({
//     next: (pdfBlob: Blob) => {
//       const url = window.URL.createObjectURL(pdfBlob);

//       const a = document.createElement('a');
//       a.href = url;
//       a.download = `etudiant-${this.etudiant.num_etudiant}.pdf`;
//       a.click();

//       window.URL.revokeObjectURL(url);
//     },
//     error: (err) => {
//       console.error('Erreur génération PDF', err);
//     }
//   });
// }

  

}
