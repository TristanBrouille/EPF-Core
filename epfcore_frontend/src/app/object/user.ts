export interface User {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  birthday: Date;
}

export interface UserLog{
  email: string;
  password: string;
}
