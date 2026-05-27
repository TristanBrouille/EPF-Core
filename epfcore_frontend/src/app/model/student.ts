import { User } from './user';

export interface Student {
  id: number;
  studentNumber: string;
  user: User;
  gender: string;
  nationality: string;
  scholarship: string;
  address: string;
  phone: string;
  program: string;
  academicYear: string;
  major: string;
  campus: string;
  enrollmentDate: string;
  lastDegree: string;
  photoUrl?: string;
}



