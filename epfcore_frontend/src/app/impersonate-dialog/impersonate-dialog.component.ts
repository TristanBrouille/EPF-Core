import { Component, OnInit } from '@angular/core';
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {Observable, firstValueFrom, switchMap, from} from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ImpersonateService } from './impersonate.service';
import { UserDto } from '../model/user';

@Component({
  selector: 'app-impersonate-dialog',
  templateUrl: './impersonate-dialog.component.html',
  styleUrls: ['./impersonate-dialog.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    MatDialogActions,
    MatDialogContent,
    MatDialogClose,
    MatDialogTitle
  ]
})
export class ImpersonateDialogComponent implements OnInit {
  searchControl = new FormControl('', { nonNullable: true });
  filteredUsers$!: Observable<UserDto[]>;

  constructor(
    public dialogRef: MatDialogRef<ImpersonateDialogComponent>,
    private impersonateService: ImpersonateService
  ) {}

  ngOnInit(): void {
    const users$ = from(this.impersonateService.getAllUsers());

    this.filteredUsers$ = this.searchControl.valueChanges.pipe(
      startWith(''),
      switchMap(searchTerm =>
        users$.pipe(
          map(users => this._filter(users, searchTerm))
        )
      )
    );
  }

  private _filter(users: UserDto[], value: string): UserDto[] {
    const filterValue = value.toLowerCase().trim();
    if (!filterValue) return users;

    return users.filter(user =>
      (user.firstname?.toLowerCase().includes(filterValue)) ||
      (user.lastname?.toLowerCase().includes(filterValue))
    );
  }

  async impersonateUser(user: UserDto): Promise<void> {
    try {
      await this.impersonateService.impersonate(user.id);
      this.dialogRef.close(true);
    } catch (error) {
      console.error('Failed to impersonate user', error);
    }
  }
}
