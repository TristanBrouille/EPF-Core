import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormulaireService } from '../../formulaire-inscription/formulaireService';
import { DocumentService } from '../../formulaire-inscription/documentService';
import { Formulaire } from '../../model/formulaire';
import { DocumentFormulaire } from '../../model/document-formulaire';
import { EntretienService } from '../entretien-service';
import { EntretienCandidature } from '../../model/entretien';

export interface DocumentTypeDef {
  key: string;
  label: string;
}

@Component({
  selector: 'app-formulaire-detail',
  imports: [],
  templateUrl: './formulaire-detail.html',
  styleUrl: './formulaire-detail.scss',
})
export class FormulaireDetail implements OnInit {
  formulaire: Formulaire | null = null;
  documents: Map<string, DocumentFormulaire> = new Map();
  entretien: EntretienCandidature | null = null;
  errorMessage = '';
  formulaireId?: number;

  readonly documentTypes: DocumentTypeDef[] = [
    { key: 'CV', label: 'CV' },
    { key: 'LETTRE_MOTIVATION', label: 'Lettre de motivation' },
    { key: 'CNI', label: "Carte d'identité" },
    { key: 'DIPLOME', label: 'Diplôme' },
  ];

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly formulaireService: FormulaireService,
    private readonly documentService: DocumentService,
    private readonly entretienService: EntretienService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  async ngOnInit(): Promise<void> {
    this.formulaireId = Number(this.route.snapshot.paramMap.get('id'));
    try {
      this.formulaire = await this.formulaireService.getById(this.formulaireId);
      const documents = await this.documentService.getDocuments(this.formulaireId);
      this.documents = new Map(documents.map(d => [d.documentType, d]));
      const entretiens = await this.entretienService.getByFormulaire(this.formulaireId);
      this.entretien = entretiens.find(e => e.statut === 'PLANIFIE' || e.statut === 'REALISE') ?? null;
    } catch (error) {
      this.errorMessage = 'Impossible de récupérer le dossier de candidature';
      console.error(error);
    }
    this.cdr.detectChanges();
  }

  isDocumentProvided(documentType: string): boolean {
    return this.documents.has(documentType);
  }

  getFileName(documentType: string): string {
    return this.documents.get(documentType)?.fileName ?? 'Aucun fichier';
  }

  private getMimeType(fileName: string): string {
    const extension = fileName.split('.').pop()?.toLowerCase();
    switch (extension) {
      case 'pdf':
        return 'application/pdf';
      case 'jpg':
      case 'jpeg':
        return 'image/jpeg';
      case 'png':
        return 'image/png';
      default:
        return 'application/octet-stream';
    }
  }

  async previewDocument(documentType: string): Promise<void> {
    if (!this.formulaireId) {
      return;
    }
    try {
      const blob = await this.documentService.download(this.formulaireId, documentType);
      const typedBlob = new Blob([blob], { type: this.getMimeType(this.getFileName(documentType)) });
      const url = window.URL.createObjectURL(typedBlob);
      window.open(url, '_blank');
      setTimeout(() => window.URL.revokeObjectURL(url), 10000);
    } catch (error) {
      this.errorMessage = 'Impossible d\'afficher l\'aperçu du document';
      console.error(error);
      this.cdr.detectChanges();
    }
  }

  goBack(): void {
    this.router.navigate(['/admin/formulaires']);
  }

  createEntretien(): void {
    this.router.navigate(['/admin/entretiens/nouveau'], { queryParams: { formulaireId: this.formulaireId } });
  }

  voirEntretien(): void {
    this.router.navigate(['/admin/entretiens', this.entretien?.id]);
  }
}
