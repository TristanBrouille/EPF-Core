export interface DocumentStudent {
  id: number;
  userId: number;
  documentType: DocumentType;
  status: DocumentStatus;
  creationDate: string;
  processingDate?: string;
  fileUrl?: string;
}

export enum DocumentType {
  CERTIFICATE_SCOLAR = 'CERTIFICATE_SCOLAR',
  INFOS_STUDENT = 'INFOS_STUDENT',
}

export enum DocumentStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}

