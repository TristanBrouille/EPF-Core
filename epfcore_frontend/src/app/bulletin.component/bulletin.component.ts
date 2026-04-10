
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BulletinData, Semester, CourseGroup, SubCourse } from './bulletin.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-bulletin',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './bulletin.component.html',
  styleUrls: ['./bulletin.component.scss']
})
export class BulletinComponent {
  @Input() data: BulletinData = SAMPLE_DATA;
}

// ── Sample data matching the image ──────────────────────────────────────────
const SAMPLE_DATA: BulletinData = {
  studentName: 'Jean Dupont',
  studentId: '123456',
  program: 'Cycle Ingénieur',
  academicYear: '2024–2025',
  summary: {
    rank: 48,
    students: 260,
    classAverage: 11.56,
    stdDev: 2.07,
    average: 13.47,
    ectsSession1: 57.0,
    ectsSession2: 60.0,
    ectsMax: 60.0,
  },
  semesters: [
    {
      label: 'Semestre 1',
      labelEn: '',
      rank: 42,
      students: 261,
      classAverage: 11.80,
      stdDev: 1.79,
      average: 13.51,
      ectsSession1: 27.0,
      ectsSession2: 30.0,
      ectsMax: 30.0,
      groups: [
        {
          label: 'Mathématiques pour l\'Ingénieur 1',
          labelEn: ' ',
          students: 260,
          classAverage: 11.62,
          stdDev: 2.18,
          average: 13.60,
          ectsSession1: 7.0,
          ectsSession2: 7.0,
          ectsMax: 7.0,
          grade: 'B',
          subCourses: [
            { label: 'Mathématiques et Abstraction 1', labelEn: ' ', students: 261, classAverage: 10.89, stdDev: 2.85, average: 12.58, ectsMax: 3.0 },
            { label: 'Outils Mathématiques pour l\'Ingénieur 1', labelEn: ' ', students: 261, classAverage: 12.44, stdDev: 2.28, average: 13.90, ectsMax: 3.0 },
          ]
        },
        {
          label: 'Sciences Physiques 1',
          labelEn: ' ',
          students: 261,
          classAverage: 10.22,
          stdDev: 2.02,
          average: 10.19,
          ectsSession1: 4.0,
          ectsSession2: 7.0,
          ectsMax: 7.0,
          grade: 'E',
          subCourses: [
            { label: 'Signaux et Systèmes Physiques', labelEn: ' ', students: 261, classAverage: 10.29, stdDev: 2.56, average: 10.80, ectsMax: 1.5 },
            { label: 'Circuits Électriques 1', labelEn: ' ', students: 261, classAverage: 10.93, stdDev: 2.58, average: 11.20, ectsMax: 1.5 },
            { label: 'Chimie des Solutions', labelEn: ' ', students: 261, classAverage: 9.05, stdDev: 2.82, average: 8.25, ectsMax: 1.5 },
            { label: 'Examen Physique 1', labelEn: '', students: 261, classAverage: 8.21, stdDev: 3.40, average: 6.96, ectsSession2Special: '*', ectsMax: 1.5 },
          ]
        },
        {
          label: 'Sciences de l\'Ingénieur 1',
          labelEn: ' ',
          students: 258,
          classAverage: 10.34,
          stdDev: 2.12,
          average: 13.85,
          ectsSession1: 4.0,
          ectsSession2: 4.0,
          ectsMax: 4.0,
          grade: 'B',
          subCourses: [
            { label: 'Analyse des Systèmes et Technologie Mécanique 1', labelEn: ' ', students: 259, classAverage: 10.34, stdDev: 2.21, average: 13.85, ectsMax: 3.0 },
            { label: 'Introduction aux Outils de l\'Ingénierie Numérique 1', labelEn: 'I ', students: null, classAverage: null, stdDev: null, average: null, ectsSession1Special: 'Quitus', ectsMax: 1.0 },
          ]
        }
      ]
    },
    {
      label: 'Semestre 2',
      labelEn: '',
      rank: 42,
      students: 261,
      classAverage: 11.80,
      stdDev: 1.79,
      average: 13.51,
      ectsSession1: 27.0,
      ectsSession2: 30.0,
      ectsMax: 30.0,
      groups: [
        {
          label: 'Mathématiques pour l\'Ingénieur 2',
          labelEn: ' ',
          students: 260,
          classAverage: 11.62,
          stdDev: 2.18,
          average: 13.60,
          ectsSession1: 7.0,
          ectsSession2: 7.0,
          ectsMax: 7.0,
          grade: 'B',
          subCourses: [
            { label: 'Outils Mathématiques pour l\'Ingénieur 2', labelEn: ' ', students: 261, classAverage: 12.44, stdDev: 2.28, average: 13.90, ectsMax: 3.0 },
            { label: 'Techniques Calculatoires', labelEn: ' ', students: 261, classAverage: 11.33, stdDev: 3.16, average: 15.78, ectsMax: 1.0 },
          ]
        },
        {
          label: 'Sciences Physiques 2',
          labelEn: ' ',
          students: 261,
          classAverage: 10.22,
          stdDev: 2.02,
          average: 10.19,
          ectsSession1: 4.0,
          ectsSession2: 7.0,
          ectsMax: 7.0,
          grade: 'E',
          subCourses: [
            { label: 'Circuits Électriques 2', labelEn: ' ', students: 261, classAverage: 10.93, stdDev: 2.58, average: 11.20, ectsMax: 1.5 },
            { label: 'Optique', labelEn: ' ', students: 261, classAverage: 13.76, stdDev: 1.53, average: 15.53, ectsMax: 1.0 },
            { label: 'Examen Physique 2', labelEn: '', students: 261, classAverage: 8.21, stdDev: 3.40, average: 6.96, ectsSession2Special: '*', ectsMax: 1.5 },
          ]
        },
        {
          label: 'Sciences de l\'Ingénieur 2',
          labelEn: ' ',
          students: 258,
          classAverage: 10.34,
          stdDev: 2.12,
          average: 13.85,
          ectsSession1: 4.0,
          ectsSession2: 4.0,
          ectsMax: 4.0,
          grade: 'B',
          subCourses: [
            { label: 'Analyse des Systèmes et Technologie Mécanique 2', labelEn: ' ', students: 259, classAverage: 10.34, stdDev: 2.21, average: 13.85, ectsMax: 3.0 },
            { label: 'Introduction aux Outils de l\'Ingénierie Numérique 2', labelEn: 'I ', students: null, classAverage: null, stdDev: null, average: null, ectsSession1Special: 'Quitus', ectsMax: 1.0 },
          ]
        }
      ]
    }
  ]
};