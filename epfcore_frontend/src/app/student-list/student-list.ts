import { Component, OnInit, Inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Student } from '../model/student';
import { StudentService } from '../student-profile/student-service';

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-list.html',
  styleUrl: './student-list.scss',
})
export class StudentList implements OnInit {
  students: Student[] = [];
  loading = true;
  error = '';
  searchTerm = '';

  constructor(
    @Inject(StudentService) private studentService: StudentService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.studentService.getAll().then(students => {
      this.students = students;
      this.loading = false;
      this.cdr.detectChanges();
    }).catch(err => {
      console.error(err);
      this.error = 'Erreur lors du chargement des étudiants';
      this.loading = false;
      this.cdr.detectChanges();
    });
  }

  get filteredStudents(): Student[] {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) return this.students;
    return this.students.filter(s =>
      s.user.firstname.toLowerCase().includes(term) ||
      s.user.lastname.toLowerCase().includes(term) ||
      s.user.email.toLowerCase().includes(term)
    );
  }

  goToStudent(id: number): void {
    this.router.navigate(['/student-search', id]);
  }
}