import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { SalleApi } from '../service/salleApi';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-liste-salles',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './listeSalles.html',
  styleUrls: ['./listeSalles.scss']
})
export class ListeSallesComponent implements OnInit {
  salles: any[] = [];           
  sallesFiltrees: any[] = [];   
  campusDisponibles: string[] = [];
  campusSelectionne: string = '';

  constructor(
    private salleApi: SalleApi,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.chargerSalles();
  }

  chargerSalles(): void {
    this.salleApi.getSalles().subscribe({
      next: (data) => {
        this.salles = data;
        this.sallesFiltrees = data;
        this.extraireCampus();
      },
      error: (err) => console.error('Erreur lors du chargement des salles', err)
    });
  }

  extraireCampus(): void {
    const tousLesCampus = this.salles
      .map(s => s.campus?.ville)
      .filter(ville => ville !== undefined && ville !== null);
    
    this.campusDisponibles = Array.from(new Set(tousLesCampus));
  }

  filtrerParCampus(): void {
    if (!this.campusSelectionne) {
      this.sallesFiltrees = this.salles;
    } else {
      this.sallesFiltrees = this.salles.filter(s => s.campus?.ville === this.campusSelectionne);
    }
  }

  allerACreerSalle(): void {
    this.router.navigate(['/creerSalle']);
  }

  allerAListeReservations(): void {
    this.router.navigate(['/listeReservations']);
  }
}