package com.epfcore.epfcore.grades.dto;

public class NoteDTO {
    public Long    id;
    public Long    etudiantId;
    public Long    evaluationId;
    public Float   valeurNote;
    public boolean absent;
    public String  commentaire;

    public NoteDTO(com.epfcore.epfcore.grades.entities.Note n) {
        this.id          = n.getId();
        this.etudiantId  = n.getEtudiantId();
        this.evaluationId = n.getEvaluation().getId();
        this.valeurNote  = n.getValeurNote();
        this.absent      = n.isAbsent();
        this.commentaire = n.getCommentaire();
    }

}
