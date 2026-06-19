# Fiche Technique des Tests Unitaires - epfcore

Ce document détaille les tests unitaires mis en place pour les services de l'application `epfcore`.

---

## 1. Module `user`

### 1.1. Fiche Technique : `AdminServiceTest`

- **Service Testé**: `com.epfcore.epfcore.user.service.AdminService`
- **Classe de Test**: `com.epfcore.epfcore.user.service.AdminServiceTest`
- **Dépendances Mockées**:
    - `UserRepository`
    - `UserDetailsService`
    - `SecurityContextRepository`
    - `HttpServletRequest`, `HttpServletResponse`, `HttpSession`
- **Scénarios Testés**:
    - `getAllUsers()`: Vérification de la récupération de tous les utilisateurs.
    - `impersonate()`: Simulation de l'usurpation d'identité d'un utilisateur.
    - `revertImpersonation()`: Simulation du retour à l'identité originale après usurpation.

### 1.2. Fiche Technique : `UserAdminServiceTest`

- **Service Testé**: `com.epfcore.epfcore.user.service.UserAdminService`
- **Classe de Test**: `com.epfcore.epfcore.user.service.UserAdminServiceTest`
- **Dépendances Mockées**:
    - `UserRepository`
    - `PasswordEncoder`
- **Scénarios Testés**:
    - `getUserById()`: Récupération d'un utilisateur par son ID.
    - `updateUser()`: Mise à jour des informations d'un utilisateur, y compris le mot de passe.

---

## 2. Module `email`

### 2.1. Fiche Technique : `EmailServiceTest`

- **Service Testé**: `com.epfcore.epfcore.email.EmailService`
- **Classe de Test**: `com.epfcore.epfcore.email.EmailServiceTest`
- **Dépendances Mockées**:
    - `JavaMailSender`
    - `GenerationLettreDecisionService`
    - `MimeMessage`
- **Scénarios Testés**:
    - `sendDecisionEmail()`: Vérification de l'envoi de l'email de décision d'admission.
    - `sendEntretienEmail()`: Vérification de l'envoi de l'email de convocation à un entretien.

### 2.2. Fiche Technique : `GenerationLettreDecisionServiceTest`

- **Service Testé**: `com.epfcore.epfcore.email.GenerationLettreDecisionService`
- **Classe de Test**: `com.epfcore.epfcore.email.GenerationLettreDecisionServiceTest`
- **Dépendances Mockées**: Aucune (logique interne de génération de PDF).
- **Scénarios Testés**:
    - `generate()`: Vérification que la génération du PDF produit un tableau de bytes non nul et non vide.

---

## 3. Module `grades`

### 3.1. Fiche Technique : `NoteServiceTest`

- **Service Testé**: `com.epfcore.epfcore.grades.services.NoteService`
- **Classe de Test**: `com.epfcore.epfcore.grades.services.NoteServiceTest`
- **Dépendances Mockées**:
    - `EntityManager`
    - `TypedQuery`
- **Scénarios Testés**:
    - `creerCarnet()`: Création d'un carnet de notes.
    - `findAllCarnets()`: Récupération de tous les carnets.
    - `publierCarnet()`: Publication d'un carnet.
    - `ajouterEvaluation()`: Ajout d'une évaluation à un carnet.
    - `saisirNote()`: Saisie manuelle d'une note.
    - `importerNotesCSV()`: Import de notes depuis un fichier CSV.
    - `calculerMoyenneEtudiant()`: Calcul de la moyenne d'un étudiant pour un carnet.

---

## 4. Module `student`

### 4.1. Fiche Technique : `StudentServiceTest`

- **Service Testé**: `com.epfcore.epfcore.student.service.StudentService`
- **Classe de Test**: `com.epfcore.epfcore.student.service.StudentServiceTest`
- **Dépendances Mockées**:
    - `StudentRepository`
- **Scénarios Testés**:
    - `getAllStudents()`: Récupération de tous les étudiants.
    - `getStudentById()`: Récupération d'un étudiant par ID (cas trouvé et non trouvé).
    - `createStudent()`: Création d'un nouvel étudiant.
    - `updateStudent()`: Mise à jour d'un étudiant existant.
    - `deleteStudent()`: Suppression d'un étudiant.

---

## 5. Module `documentStudent`

### 5.1. Fiche Technique : `DocumentStudentServiceTest`

- **Service Testé**: `com.epfcore.epfcore.documentStudent.service.DocumentStudentService`
- **Classe de Test**: `com.epfcore.epfcore.documentStudent.service.DocumentStudentServiceTest`
- **Dépendances Mockées**:
    - `DocumentStudentRepository`
    - `StudentRepository`
- **Scénarios Testés**:
    - `getById()`, `create()`, `archive()`, `unarchive()`
    - `update()`: Protection contre la modification d'un document archivé.
    - `delete()`: Protection contre la suppression d'un document archivé.

### 5.2. Fiche Technique : `GenerationPdfServiceTest`

- **Service Testé**: `com.epfcore.epfcore.documentStudent.service.GenerationPdfService`
- **Classe de Test**: `com.epfcore.epfcore.documentStudent.service.GenerationPdfServiceTest`
- **Dépendances Mockées**:
    - `DocumentStudentRepository`, `StudentRepository`
- **Scénarios Testés**:
    - `generateFromRequest()`: Génération d'un PDF (cas avec document existant et nouveau document).

### 5.3. Fiche Technique : `GenerationCertificateServiceTest`

- **Service Testé**: `com.epfcore.epfcore.documentStudent.service.GenerationCertificateService`
- **Classe de Test**: `com.epfcore.epfcore.documentStudent.service.GenerationCertificateServiceTest`
- **Dépendances Mockées**:
    - `DocumentStudentRepository`, `StudentRepository`
- **Scénarios Testés**:
    - `generateFromRequest()`: Génération d'un certificat (cas avec document existant et nouveau document).

---

## 6. Module `entretienCandidature`

### 6.1. Fiche Technique : `EntretienCandidatureServiceTest`

- **Service Testé**: `com.epfcore.epfcore.entretienCandidature.service.EntretienCandidatureService`
- **Classe de Test**: `com.epfcore.epfcore.entretienCandidature.service.EntretienCandidatureServiceTest`
- **Dépendances Mockées**:
    - `EntretienCandidatureRepository`, `FormulaireInscriptionRepository`, `UserJpaRepository`, `CampusRepository`, `EmailService`
- **Scénarios Testés**:
    - `create()`: Création d'un entretien et envoi de l'email de notification.

---

## 7. Module `formulaireInscription`

### 7.1. Fiche Technique : `FormulaireInscriptionServiceTest`

- **Service Testé**: `com.epfcore.epfcore.formulaireInscription.service.FormulaireInscriptionService`
- **Classe de Test**: `com.epfcore.epfcore.formulaireInscription.service.FormulaireInscriptionServiceTest`
- **Dépendances Mockées**:
    - `FormulaireInscriptionRepository`, `UserJpaRepository`, `CampusRepository`, `DocumentFormulaireRepository`, `StorageService`, `EmailService`, `StudentRepository`
- **Scénarios Testés**:
    - `save()`: Création d'un nouveau formulaire d'inscription.
    - `updateDecision()`: Mise à jour de la décision d'admission et envoi de l'email de notification.

---

