export interface BulletinSummary {
  rank: number | null;
  students: number;
  classAverage: number | null;
  stdDev: number | null;
  average: number | null;
  ectsSession1: number | null;
  ectsSession2: number | null;
  ectsMax: number | null;
}

export interface SubCourse {
  label: string;
  labelEn: string;
  rank?: number | null;
  students: number | null;
  classAverage: number | null;
  stdDev: number | null;
  average: number | null;
  ectsSession1?: number | null;
  ectsSession1Special?: string;
  ectsSession2?: number | null;
  ectsSession2Special?: string;
  ectsMax: number | null;
  grade?: string;
}

export interface CourseGroup extends SubCourse {
  subCourses: SubCourse[];
}

export interface Semester {
  label: string;
  labelEn: string;
  rank: number | null;
  students: number;
  classAverage: number | null;
  stdDev: number | null;
  average: number | null;
  ectsSession1: number | null;
  ectsSession2: number | null;
  ectsMax: number | null;
  groups: CourseGroup[];
}

export interface BulletinData {
  studentName: string;
  studentId: string;
  program: string;
  academicYear: string;
  summary: BulletinSummary;
  semesters: Semester[];
}
