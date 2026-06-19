export interface User {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
  birthDate: Date;
  role : string;
}

export interface UserLog{
  email: string;
  password: string;
}

export interface UserRole {
  email: string;
  authorities: string[];
}

export interface UserDto {
  id: number;
  firstname: string;
  lastname: string;
  role : string;
}

export interface RegistrationData {
  firstname: string;
  lastname: string;
  email: string;
  password?: string;
  birthDate: string;
  role: string;
  idRfid: string;
}

export interface UserAdmin {
  id?: number;
  firstname: string;
  lastname: string;
  email: string;
  password?: string;
  birthDate: string;
  role: string;
  idRfid: string;
}
