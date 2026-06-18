import { Component, OnInit, DestroyRef, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ReservationApi } from '../service/reservation.service'; // Ajuste selon ton projet
import { HttpClient } from '@angular/common/http'; // Pour aller chercher les campus si besoin
import { Reservation } from '../model/reservation';

@Component({
  selector: 'app-liste-reservations',
  standalone: true,
  imports: [CommonModule, DatePipe, RouterLink],
  templateUrl: './listeReservations.html',
  styleUrls: ['./listeReservations.scss']
})
export class ListeReservationsComponent implements OnInit {
  private readonly reservationApi = inject(ReservationApi);
  private readonly http = inject(HttpClient);
  private readonly destroyRef = inject(DestroyRef);
  private readonly cdr = inject(ChangeDetectorRef);

  reservations: any[] = [];
  reservationsFiltrees: any[] = [];

  // Filtres
  filtrePeriode: 'tous' | 'futur' | 'passe' = 'tous';
  filtreStatus: 'TOUS' | 'EN_ATTENTE' | 'ACCEPTE' | 'REFUSE' = 'TOUS';
 
  ngOnInit(): void {
    this.chargerReservations();
  }

  chargerReservations(): void {
    this.reservationApi.getReservations()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (data) => {
          this.reservations = data;
          this.appliquerFiltres();
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Erreur lors du chargement des réservations', err)
      });
  }

  appliquerFiltres(): void {
    const aujourdhui = new Date();
    aujourdhui.setHours(0, 0, 0, 0);

    this.reservationsFiltrees = this.reservations.filter(res => {
      let dateRes: Date;
      if (res.date && res.date.year) {
        dateRes = new Date(res.date.year, res.date.month - 1, res.date.day);
      } else {
        const [annee, mois, jour] = res.date.split('-').map(Number);
        dateRes = new Date(annee, mois - 1, jour);
      }
      dateRes.setHours(0, 0, 0, 0);

      let matchPeriode = true;
      if (this.filtrePeriode === 'futur') matchPeriode = dateRes >= aujourdhui;
      if (this.filtrePeriode === 'passe') matchPeriode = dateRes < aujourdhui;

      let matchStatus = true;
      if (this.filtreStatus !== 'TOUS') matchStatus = res.status === this.filtreStatus;

      return matchPeriode && matchStatus;
    });
  }

  setFiltrePeriode(periode: 'tous' | 'futur' | 'passe'): void {
    this.filtrePeriode = periode;
    this.appliquerFiltres();
  }

  setFiltreStatus(status: 'TOUS' | 'EN_ATTENTE' | 'ACCEPTE' | 'REFUSE'): void {
    this.filtreStatus = status;
    this.appliquerFiltres();
  }

  modifierStatut(id: number, nouveauStatut: 'ACCEPTE' | 'REFUSE'): void {
  
  const res = this.reservations.find(r => r.idReservation === id);
  if (!res) return;

  const maj = { ...res, status: nouveauStatut };

  this.reservationApi.updateReservation(id, maj)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: () => {
        alert(`La réservation a été ${nouveauStatut === 'ACCEPTE' ? 'acceptée' : 'refusée'} !`);
        this.chargerReservations();
      },
      error: (err) => {
        console.error("Erreur lors de la mise à jour :", err);
      }
    });
}
}