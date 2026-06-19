import { User } from './user';

export interface DocumentStudent {
  id: number;
  documentType: string;
  status: string;
  creationDate: string;
  processingDate?: string;
  archivedAt?: string;
  archivedBy?: string;
  userId: number;
  firstname: string;
  lastname: string;
  studentId: number;
}

export enum DocumentType {
  CERTIFICATE_SCOLAR = 'CERTIFICATE_SCOLAR',
  INFOS_STUDENT = 'INFOS_STUDENT',
}

export enum DocumentStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  ARCHIVED = 'ARCHIVED'
}

