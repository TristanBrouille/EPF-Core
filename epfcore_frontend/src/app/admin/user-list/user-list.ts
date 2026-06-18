import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {UserAdmin, UserDto} from '../../model/user';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AdminService} from '../admin.service';
import {NgClass} from '@angular/common';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-list',
  imports: [
    NgClass,
    ReactiveFormsModule
  ],
  templateUrl: './user-list.html',
  styleUrl: './user-list.scss',
})
export class UserList implements OnInit {
  users: UserDto[] = [];
  roles: string[] = [];
  editForm!: FormGroup;

  selectedUser: UserAdmin | null = null;
  isLoading = false;
  message: string | null = null;
  isError = false;

  constructor(
    private userService: AdminService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.initForm();
    await this.loadUsers();
    await this.loadRoles();
  }

  initForm(): void {
    this.editForm = this.fb.group({
      firstname: ['', [Validators.required, Validators.minLength(2)]],
      lastname: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: [''],
      birthDate: ['', Validators.required],
      role: ['', Validators.required],
      idRfid: ['', [Validators.required, Validators.pattern(/^RF\d+$/)]]
    });
  }

  async loadUsers(): Promise<void> {
    this.isLoading = true;
    try {
      this.users = await this.userService.getAllUsers();

      this.cdr.detectChanges();

    } catch (err) {
      console.error("Erreur chargement liste :", err);
    } finally {
      this.isLoading = false;
      this.cdr.detectChanges();
    }
  }

  async loadRoles(): Promise<void> {
    try {
      this.roles = await this.userService.roles();
    } catch (err) {
      console.error(err);
    }
  }

  async selectUser(userDto: UserDto): Promise<void> {
    this.message = null;
    this.isLoading = true;

    try {
      this.selectedUser = await this.userService.getUserById(userDto.id);
      this.editForm.patchValue({
        firstname: this.selectedUser.firstname,
        lastname: this.selectedUser.lastname,
        email: this.selectedUser.email,
        password: '',
        birthDate: this.selectedUser.birthDate,
        role: this.selectedUser.role,
        idRfid: this.selectedUser.idRfid
      });
    } catch (err) {
      console.error("Impossible de charger les détails de l'utilisateur", err);
    } finally {
      this.isLoading = false;
    }
  }

  async onUpdate(): Promise<void> {
    if (this.editForm.invalid || !this.selectedUser?.id) return;

    this.isLoading = true;
    this.message = null;

    try {
      const updatedData: UserAdmin = { ...this.editForm.value };

      if (!updatedData.password || updatedData.password.trim() === '') {
        delete updatedData.password;
      }

      await this.userService.updateUser(this.selectedUser.id, updatedData);

      this.isError = false;
      this.message = "Utilisateur patché avec succès !";
      this.selectedUser = null;
      await this.loadUsers();
    } catch (err) {
      this.isError = true;
      this.message = "Erreur lors de la modification.";
      console.error(err);
    } finally {
      this.isLoading = false;
    }
  }

  navigateToCreate(): void {
    this.router.navigate(['/registration']);
  }

}
