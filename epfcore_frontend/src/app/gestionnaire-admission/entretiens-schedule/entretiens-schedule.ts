import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { EntretienService } from '../entretien-service';
import { EntretienCandidature } from '../../model/entretien';

@Component({
  selector: 'app-entretiens-schedule',
  imports: [DatePipe],
  templateUrl: './entretiens-schedule.html',
  styleUrl: './entretiens-schedule.scss',
})
export class EntretiensSchedule implements OnInit {
  entretiens: EntretienCandidature[] = [];
  errorMessage = '';
  weekStart: Date = this.getMonday(new Date());

  private readonly dayNames = ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'];

  constructor(
    private readonly entretienService: EntretienService,
    private readonly router: Router,
    private readonly cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    try {
      this.entretiens = await this.entretienService.getAll();
    } catch (error) {
      this.errorMessage = 'Impossible de récupérer les entretiens';
      console.error(error);
    }
    this.cdr.detectChanges();
  }

  private getMonday(date: Date): Date {
    const d = new Date(date);
    const day = d.getDay();
    const diff = (day === 0 ? -6 : 1) - day;
    d.setDate(d.getDate() + diff);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  get weekDays(): Date[] {
    return Array.from({ length: 6 }, (_, i) => {
      const d = new Date(this.weekStart);
      d.setDate(d.getDate() + i);
      return d;
    });
  }

  previousWeek(): void {
    const d = new Date(this.weekStart);
    d.setDate(d.getDate() - 7);
    this.weekStart = d;
  }

  nextWeek(): void {
    const d = new Date(this.weekStart);
    d.setDate(d.getDate() + 7);
    this.weekStart = d;
  }

  dayLabel(date: Date): string {
    return `${this.dayNames[date.getDay()]} ${date.getDate().toString().padStart(2, '0')}/${(date.getMonth() + 1).toString().padStart(2, '0')}`;
  }

  timeLabel(dateHeure: string): string {
    const d = new Date(dateHeure);
    return `${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`;
  }

  entretiensForDay(day: Date): EntretienCandidature[] {
    return this.entretiens
      .filter(e => new Date(e.dateHeure).toDateString() === day.toDateString())
      .sort((a, b) => new Date(a.dateHeure).getTime() - new Date(b.dateHeure).getTime());
  }

  openEntretien(id?: number): void {
    if (id) {
      this.router.navigate(['/admin/entretiens', id]);
    }
  }

  createEntretien(): void {
    this.router.navigate(['/admin/entretiens/nouveau']);
  }
}
