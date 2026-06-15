import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Salle, Campus } from '../model/salle';
import { Router } from '@angular/router';
import { SalleApi } from '../service/salleApi';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-creerSalle',
  standalone:true,
  imports: [FormsModule, CommonModule],
  templateUrl: './creerSalle.html',
  styleUrls: ['./creerSalle.scss'],
})

export class creerSalleComponent implements OnInit {

  private readonly salleApi = inject(SalleApi); // Votre service
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  // 2. MODIFIE LA DÉCLARATION ICI : remplace string[] par Campus[]
  campusList: any[] = []; 
  typeSalleList: string[] = []; 

  // 3. METS À JOUR TON OBJET SALLE ICI
  salle = {
    id: undefined,
    nomSalle: '',
    capacite: 0,
    equipement: '',
    campus: null,
    typeSalle: ''
  };

ngOnInit(): void {
  this.salleApi.getCampusOptions().subscribe({
    next: (data) => {
      this.campusList = data;
      // Optionnel : sélection du premier ID par défaut
      if (data.length > 0 && !this.salle.campus) {
        this.salle.campus = data[0].id;
      }
    },
    error: (err) => console.error(err)
  });

    // Récupération des Types de Salles depuis le Back
    this.salleApi.getTypeSalleEnums().subscribe({
      next: (data) => {
        this.typeSalleList = data;
        if (data.length > 0 && !this.salle.typeSalle) {
          this.salle.typeSalle = data[0]; // Valeur par défaut
        }
      }
    });
  }

 saveSalle(): void {
  if (!this.salle.campus) {
    alert('Veuillez sélectionner un campus.');
    return;
  }

  // 1. On crée une copie propre de la salle pour ne pas casser le formulaire HTML
  const salleToSave = { 
    ...this.salle,
    // On transforme l'ID numérique en l'objet complet attendu par Spring Boot
    campus: { id: Number(this.salle.campus) } 
  };

  // 2. On envoie "salleToSave" au lieu de "this.salle"
  if (this.salle.id) {
    // Cas du UPDATE
    this.salleApi.updateSalle(salleToSave as any).subscribe({
      next: () => {
        alert('La salle a été mise à jour avec succès !');
        this.router.navigate(['/salles']);
      },
      error: (err) => console.error('Erreur lors du UPDATE :', err)
    });
  } else {
    // Cas du CREATE
    this.salleApi.addSalle(salleToSave as any).subscribe({
      next: () => {
        alert('La salle a été créée avec succès !');
        this.router.navigate(['/salles']);
      },
      error: (err) => console.error('Erreur lors du CREATE :', err)
    });
  }
}
}