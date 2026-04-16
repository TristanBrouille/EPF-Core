package com.epfcore.epfcore.etudiant.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.epfcore.epfcore.etudiant.entity.Etudiant;
import com.epfcore.epfcore.etudiant.repository.EtudiantRepository;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    public Optional<Etudiant> getEtudiantById(Long id) {
        return etudiantRepository.findById(id);
    }

    public Optional<Etudiant> getEtudiantByNumEtudiant(String numEtudiant) {
        return etudiantRepository.findByNumEtudiant(numEtudiant);
    }

    public Optional<Etudiant> getEtudiantByUserId(Long userId) {
        return etudiantRepository.findByUserId(userId);
    }

    public Etudiant createEtudiant(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    public Etudiant updateEtudiant(Long id, Etudiant updatedEtudiant) {
        return etudiantRepository.findById(id)
                .map(etudiant -> {
                    etudiant.setNumEtudiant(updatedEtudiant.getNumEtudiant());
                    etudiant.setUser(updatedEtudiant.getUser());
                    etudiant.setSexe(updatedEtudiant.getSexe());
                    etudiant.setNationalite(updatedEtudiant.getNationalite());
                    etudiant.setTelephone(updatedEtudiant.getTelephone());
                    etudiant.setAdresse(updatedEtudiant.getAdresse());
                    etudiant.setAnneeAcademique(updatedEtudiant.getAnneeAcademique());
                    etudiant.setFormation(updatedEtudiant.getFormation());
                    etudiant.setProgramme(updatedEtudiant.getProgramme());
                    etudiant.setCampus(updatedEtudiant.getCampus());
                    etudiant.setBourse(updatedEtudiant.getBourse());
                    etudiant.setDernierDiplome(updatedEtudiant.getDernierDiplome());
                    etudiant.setLienPhoto(updatedEtudiant.getLienPhoto());
                    etudiant.setDateInscription(updatedEtudiant.getDateInscription());

                    return etudiantRepository.save(etudiant);
                })
                .orElseThrow(() -> new RuntimeException("Etudiant non trouvé avec l'id : " + id));
    }

    public void deleteEtudiant(Long id) {
        etudiantRepository.deleteById(id);
    }
    
}
