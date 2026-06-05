package com.epfcore.epfcore.formulaireInscription.service;

import com.epfcore.epfcore.campus.entity.Campus;
import com.epfcore.epfcore.campus.repository.CampusRepository;
import com.epfcore.epfcore.formulaireInscription.dto.FormulaireInscriptionDTO;
import com.epfcore.epfcore.formulaireInscription.entity.FormulaireInscription;
import com.epfcore.epfcore.formulaireInscription.repository.FormulaireInscriptionRepository;
import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.exposition.UserExpose;
import com.epfcore.epfcore.security.infrastructure.UserJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FormulaireInscriptionService {

    private final FormulaireInscriptionRepository formulaireRepository;
    private final UserJpaRepository userRepository;
    private final CampusRepository campusRepository;

    public FormulaireInscriptionService(
            FormulaireInscriptionRepository formulaireRepository,
            UserJpaRepository userRepository,
            CampusRepository campusRepository
    ) {
        this.formulaireRepository = formulaireRepository;
        this.userRepository = userRepository;
        this.campusRepository = campusRepository;
    }

    public FormulaireInscriptionDTO save(FormulaireInscriptionDTO dto, String email) {
        User user = getUserByEmail(email);

        if (formulaireRepository.findByUserId(user.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un formulaire existe déjà pour cet utilisateur");
        }

        FormulaireInscription formulaire = toEntity(dto, user);
        return toDTO(formulaireRepository.save(formulaire));
    }

    public FormulaireInscriptionDTO update(FormulaireInscriptionDTO dto, String email) {
        User user = getUserByEmail(email);
        FormulaireInscription existing = formulaireRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formulaire not found"));

        existing.setDernierDiplome(dto.dernierDiplome());
        existing.setEtablissement(dto.etablissement());
        existing.setNiveauEtude(dto.niveauEtude());
        existing.setAnneeObtention(dto.anneeObtention());
        existing.setProgrammeChoisi(dto.programmeChoisi());
        existing.setMotivations(dto.motivations());
        existing.setSoumis(dto.soumis());
        existing.setGenre(dto.genre());
        existing.setTelephone(dto.telephone());
        existing.setNationalite(dto.nationalite());
        existing.setAdresse(dto.adresse());
        existing.setAnneeIntegration(dto.anneeIntegration());
        existing.setMajeur(dto.majeur());

        if (dto.campusVille() != null && !dto.campusVille().isEmpty()) {
            Campus campus = campusRepository.findByVille(dto.campusVille())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campus not found"));
            existing.setCampus(campus);
        }

        return toDTO(formulaireRepository.save(existing));
    }

    public FormulaireInscriptionDTO getByEmail(String email) {
        User user = getUserByEmail(email);
        return formulaireRepository.findByUserId(user.getId())
                .map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formulaire not found"));
    }

    public FormulaireInscriptionDTO getById(Long id) {
        return formulaireRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formulaire not found"));
    }

    public List<FormulaireInscriptionDTO> getAll() {
        return formulaireRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public void delete(Long id) {
        if (!formulaireRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Formulaire not found");
        }
        formulaireRepository.deleteById(id);
    }

    private FormulaireInscription toEntity(FormulaireInscriptionDTO dto, User user) {
        FormulaireInscription formulaire = new FormulaireInscription();
        formulaire.setUser(user);
        formulaire.setDernierDiplome(dto.dernierDiplome());
        formulaire.setEtablissement(dto.etablissement());
        formulaire.setNiveauEtude(dto.niveauEtude());
        formulaire.setAnneeObtention(dto.anneeObtention());
        formulaire.setProgrammeChoisi(dto.programmeChoisi());
        formulaire.setMotivations(dto.motivations());
        formulaire.setSoumis(dto.soumis());
        formulaire.setGenre(dto.genre());
        formulaire.setTelephone(dto.telephone());
        formulaire.setNationalite(dto.nationalite());
        formulaire.setAdresse(dto.adresse());
        formulaire.setAnneeIntegration(dto.anneeIntegration());
        formulaire.setMajeur(dto.majeur());

        if (dto.campusVille() != null && !dto.campusVille().isEmpty()) {
            Campus campus = campusRepository.findByVille(dto.campusVille())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campus not found"));
            formulaire.setCampus(campus);
        }

        return formulaire;
    }

    private FormulaireInscriptionDTO toDTO(FormulaireInscription formulaire) {
        User user = formulaire.getUser();
        UserExpose userExpose = new UserExpose(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getBirthDate()
        );
        return new FormulaireInscriptionDTO(
                formulaire.getId(),
                userExpose,
                formulaire.getDernierDiplome(),
                formulaire.getEtablissement(),
                formulaire.getNiveauEtude(),
                formulaire.getAnneeObtention(),
                formulaire.getProgrammeChoisi(),
                formulaire.getMotivations(),
                formulaire.getDateCreation(),
                formulaire.getCampus() != null ? formulaire.getCampus().getVille() : null,
                formulaire.getSoumis(),
                formulaire.getGenre(),
                formulaire.getTelephone(),
                formulaire.getNationalite(),
                formulaire.getAdresse(),
                formulaire.getAnneeIntegration(),
                formulaire.getMajeur()
        );
    }

    private User getUserByEmail(String email) {
        User user = userRepository.ofEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }
}