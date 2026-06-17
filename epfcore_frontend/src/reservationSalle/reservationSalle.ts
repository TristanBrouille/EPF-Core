import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SalleApi } from '../app/service/salleApi';
import { FormsModule } from '@angular/forms';
import { ReservationApi } from '../app/service/reservation.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-creer-reservation',
  imports: [CommonModule, FormsModule],
  templateUrl: './reservationSalle.html',
  styleUrls: ['./reservationSalle.scss']
})
export class CreerReservationComponent implements OnInit {
  
  salles: any[] = [];
  salleSelectionnee: any = null;

  reservation: any = {
    salleId: '',
    date: '',
    heureDebut: '',
    heureFin: '',
    nomReserveur: '', 
    nbrPersonnes: 0,
    description: '',
    status: 'EN_ATTENTE'
  };

  constructor(
    private salleApi: SalleApi,
    private reservationApi: ReservationApi,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.salleApi.getSalles().subscribe({
      next: (data) => this.salles = data,
      error: (err) => console.error('Erreur chargement salles', err)
    });

    const utilisateurConnecte = localStorage.getItem('username') || localStorage.getItem('user');
    
    if (utilisateurConnecte) {
      this.reservation.nomReserveur = utilisateurConnecte;
    } else {
      this.reservation.nomReserveur = 'Étudiant EPF'; 
    }
  }

  onSalleChange(id: string): void {
    this.salleSelectionnee = this.salles.find(s => s.id === Number(id));
  }

  saveReservation(): void {
    if (this.salleSelectionnee && this.reservation.nbrPersonnes > this.salleSelectionnee.capacite) {
      alert(`Impossible de réserver : Le nombre de personnes dépasse la capacité maximale de la salle (${this.salleSelectionnee.capacite} places).`);
      return;
    }

    const reservationToSave = {
      ...this.reservation,
      nbrPersonnes: Number(this.reservation.nbrPersonnes) || 0,
      salle: { id: Number(this.reservation.salleId) }
    };

    this.reservationApi.addReservation(reservationToSave).subscribe({
      next: () => {
        alert('Réservation créée avec succès (En attente de validation) !');
        this.router.navigate(['/']);
      },
      error: (err: any) => {
        console.error('Erreur lors du CREATE de la réservation :', err);
        
        let messageErreur = "Une erreur est survenue lors de la création de la réservation.";

        if (err.error) {
          if (typeof err.error === 'string') {
            messageErreur = err.error;
          } else if (err.error.text) {
            messageErreur = err.error.text;
          } else if (err.error.message) {
            messageErreur = err.error.message;
          }
        }
        alert(messageErreur);
      }
    });
  }
}