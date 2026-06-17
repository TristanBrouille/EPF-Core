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

  private readonly salleApi = inject(SalleApi);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  campusList: any[] = []; 
  typeSalleList: string[] = []; 

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
      if (data.length > 0 && !this.salle.campus) {
        this.salle.campus = data[0].id;
      }
    },
    error: (err) => console.error(err)
  });
    this.salleApi.getTypeSalleEnums().subscribe({
      next: (data) => {
        this.typeSalleList = data;
        if (data.length > 0 && !this.salle.typeSalle) {
          this.salle.typeSalle = data[0];
        }
      }
    });
  }

 saveSalle(): void {
  if (!this.salle.campus) {
    alert('Veuillez sélectionner un campus.');
    return;
  }

  const salleToSave = { 
    ...this.salle,
    campus: { id: Number(this.salle.campus) } 
  };

  if (this.salle.id) {
    
  } else {
    this.salleApi.addSalle(salleToSave as any).subscribe({
      next: () => {
        alert('La salle a été créée avec succès !');
        this.router.navigate(['/listeSalles']);
      },
      error: (err) => console.error('Erreur lors du CREATE :', err)
    });
  }
}
}