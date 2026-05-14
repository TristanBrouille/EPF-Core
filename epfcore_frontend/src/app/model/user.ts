export interface User {
  firstname: string;
  lastname: string;
  email: string;
  birthDate: Date;
}

export interface UserLog{
  email: string;
  password: string;
}

export interface UserRole {
  email: string;
  authorities: string[];
}
