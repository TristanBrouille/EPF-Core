import { Injectable, signal } from '@angular/core';
import { DocumentStudent, DocumentStatus } from './model/documentStudent';
import { DocumentStudentService } from '../app/history-student/documentstudent.service';
export type ArchiveAction = 'archive' | 'unarchive';
@Injectable({ providedIn: 'root' })
export class ArchiveModalService {
  showModal = signal(false);
  pendingDoc = signal<DocumentStudent | null>(null);
  onArchived = signal<number | null>(null);
  pendingAction = signal<ArchiveAction>('archive');
  onUnarchived = signal<number | null>(null);

  constructor(private documentService: DocumentStudentService) {}

  ask(doc: DocumentStudent, action: ArchiveAction): void {
    this.pendingDoc.set(doc);
    this.pendingAction.set(action);
    this.showModal.set(true);
  }

  cancel(): void {
    this.showModal.set(false);
    this.pendingDoc.set(null);
  }

  async confirm(): Promise<void> {
    const doc = this.pendingDoc();
    if (!doc) return;
    this.showModal.set(false);
    const action = this.pendingAction();

    try {
      if (action === 'archive') {
        await this.documentService.archiveDocument(doc.id, 'admin');
        this.onArchived.set(doc.id);
      } else {
        await this.documentService.unarchiveDocument(doc.id);
        this.onUnarchived.set(doc.id);
      }
    } catch (err) {
      console.error(`Erreur ${action === 'archive' ? 'archivage' : 'désarchivage'}`, err);
    } finally {
      this.pendingDoc.set(null);
    }
  }
}