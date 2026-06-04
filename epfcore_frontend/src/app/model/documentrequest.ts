import { Student } from './student';

export interface DocumentRequest {
    id: number;
    documentType: DocumentType;
    status: DocumentRequestStatus;  
    creationDate: string;       
    processingDate?: string;          
    student?: Student; 
}

export enum DocumentType {
  CERTIFICAT = 'CERTIFICAT',
  INFOS = 'INFOS',
}

export enum DocumentRequestStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}

