package com.epfcore.epfcore.formulaireInscription.service;

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

    public FormulaireInscriptionService(
            FormulaireInscriptionRepository formulaireRepository,
            UserJpaRepository userRepository
    ) {
        this.formulaireRepository = formulaireRepository;
        this.userRepository = userRepository;
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
        existing.setCampusChoisi(dto.campusChoisi());
        existing.setMotivations(dto.motivations());
        existing.setAvancement(dto.avancement());

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

    // DTO → Entité
    private FormulaireInscription toEntity(FormulaireInscriptionDTO dto, User user) {
        FormulaireInscription formulaire = new FormulaireInscription();
        formulaire.setUser(user);
        formulaire.setDernierDiplome(dto.dernierDiplome());
        formulaire.setEtablissement(dto.etablissement());
        formulaire.setNiveauEtude(dto.niveauEtude());
        formulaire.setAnneeObtention(dto.anneeObtention());
        formulaire.setProgrammeChoisi(dto.programmeChoisi());
        formulaire.setCampusChoisi(dto.campusChoisi());
        formulaire.setMotivations(dto.motivations());
        formulaire.setAvancement(dto.avancement());
        return formulaire;
    }

    // Entité → DTO
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
                formulaire.getCampusChoisi(),
                formulaire.getMotivations(),
                formulaire.getAvancement()
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