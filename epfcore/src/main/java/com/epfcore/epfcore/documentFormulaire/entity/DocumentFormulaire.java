package com.epfcore.epfcore.documentFormulaire.entity;

import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "document_formulaire")
public class DocumentFormulaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "formulaire_id", nullable = false)
    private FormulaireInscription formulaire;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @Column(name = "file_name")
    private String fileName;
}