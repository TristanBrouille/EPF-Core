export interface Document {
    id: number;
    userId: number;
    documentType: DocumentType;
    status: DocumentStatus;
    creationDate: string;
    processingDate?: string;
    fileUrl?: string;
}

export enum DocumentType {
  CERTIFICATE = 'CERTIFICATE',
  INFOS = 'INFOS',
}

export enum DocumentStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}

