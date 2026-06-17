package com.epfcore.epfcore.entretienCandidature.service;

import com.epfcore.epfcore.campus.entity.Campus;
import com.epfcore.epfcore.campus.repository.CampusRepository;
import com.epfcore.epfcore.entretienCandidature.dto.EntretienCandidatureDTO;
import com.epfcore.epfcore.entretienCandidature.dto.InterviewerDTO;
import com.epfcore.epfcore.entretienCandidature.entity.EntretienCandidature;
import com.epfcore.epfcore.entretienCandidature.entity.StatutEntretien;
import com.epfcore.epfcore.entretienCandidature.repository.EntretienCandidatureRepository;
import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import com.epfcore.epfcore.formulaireInscription.repository.FormulaireInscriptionRepository;
import com.epfcore.epfcore.security.domain.Roles;
import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.exposition.UserExpose;
import com.epfcore.epfcore.security.infrastructure.UserJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EntretienCandidatureService {

    private final EntretienCandidatureRepository entretienRepository;
    private final FormulaireInscriptionRepository formulaireRepository;
    private final UserJpaRepository userRepository;
    private final CampusRepository campusRepository;

    public EntretienCandidatureService(
            EntretienCandidatureRepository entretienRepository,
            FormulaireInscriptionRepository formulaireRepository,
            UserJpaRepository userRepository,
            CampusRepository campusRepository
    ) {
        this.entretienRepository = entretienRepository;
        this.formulaireRepository = formulaireRepository;
        this.userRepository = userRepository;
        this.campusRepository = campusRepository;
    }

    public EntretienCandidatureDTO create(EntretienCandidatureDTO dto) {
        FormulaireInscription formulaire = formulaireRepository.findById(dto.formulaireId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formulaire not found"));

        EntretienCandidature entretien = new EntretienCandidature();
        entretien.setFormulaire(formulaire);
        applyDto(entretien, dto);

        return toDTO(entretienRepository.save(entretien));
    }

    public EntretienCandidatureDTO update(Long id, EntretienCandidatureDTO dto) {
        EntretienCandidature existing = entretienRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entretien not found"));

        applyDto(existing, dto);

        return toDTO(entretienRepository.save(existing));
    }

    public EntretienCandidatureDTO getById(Long id) {
        return entretienRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entretien not found"));
    }

    public List<EntretienCandidatureDTO> getAll() {
        return entretienRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<EntretienCandidatureDTO> getByFormulaireId(Long formulaireId) {
        return entretienRepository.findByFormulaireId(formulaireId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<EntretienCandidatureDTO> getMyEntretiens(String email) {
        User user = userRepository.ofEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return entretienRepository.findByFormulaireUserId(user.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<InterviewerDTO> getInterviewers() {
        return userRepository.findByRoleIn(List.of(Roles.ENSEIGNANT, Roles.GESTIONNAIRE_ADMISSION))
                .stream()
                .map(user -> new InterviewerDTO(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail()))
                .toList();
    }

    public void delete(Long id) {
        if (!entretienRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entretien not found");
        }
        entretienRepository.deleteById(id);
    }

    private void applyDto(EntretienCandidature entretien, EntretienCandidatureDTO dto) {
        entretien.setDateHeure(dto.dateHeure());
        entretien.setSalle(dto.salle());
        entretien.setLienVisio(dto.lienVisio());
        entretien.setTypeEntretien(dto.typeEntretien());
        entretien.setStatut(dto.statut() != null ? dto.statut() : StatutEntretien.PLANIFIE);
        entretien.setNote(dto.note());
        entretien.setCommentaire(dto.commentaire());

        if (dto.campusVille() != null && !dto.campusVille().isEmpty()) {
            Campus campus = campusRepository.findByVille(dto.campusVille())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campus not found"));
            entretien.setCampus(campus);
        } else {
            entretien.setCampus(null);
        }

        if (dto.interviewerId() != null) {
            User interviewer = userRepository.findById(dto.interviewerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interviewer not found"));
            entretien.setInterviewer(interviewer);
        } else {
            entretien.setInterviewer(null);
        }
    }

    private EntretienCandidatureDTO toDTO(EntretienCandidature entretien) {
        User candidatUser = entretien.getFormulaire().getUser();
        UserExpose candidat = new UserExpose(
                candidatUser.getFirstname(),
                candidatUser.getLastname(),
                candidatUser.getEmail(),
                candidatUser.getBirthDate(),
                candidatUser.getRole()
        );

        User interviewerUser = entretien.getInterviewer();
        UserExpose interviewer = interviewerUser != null
                ? new UserExpose(
                        interviewerUser.getFirstname(),
                        interviewerUser.getLastname(),
                        interviewerUser.getEmail(),
                        interviewerUser.getBirthDate(),
                        interviewerUser.getRole()
                )
                : null;

        return new EntretienCandidatureDTO(
                entretien.getId(),
                entretien.getFormulaire().getId(),
                candidat,
                interviewerUser != null ? interviewerUser.getId() : null,
                interviewer,
                entretien.getDateHeure(),
                entretien.getCampus() != null ? entretien.getCampus().getVille() : null,
                entretien.getSalle(),
                entretien.getLienVisio(),
                entretien.getTypeEntretien(),
                entretien.getStatut(),
                entretien.getNote(),
                entretien.getCommentaire(),
                entretien.getDateCreation(),
                entretien.getFormulaire().getDecisionAdmission()
        );
    }
}
