# Documentation API - epfcore

Ce document fournit une documentation des points de terminaison (endpoints) de l'API REST de l'application `epfcore`.

---

## 1. Module `user`

### 1.1. `AdminUserController`

- **Base URL**: `/admin/users`
- **Pré-autorisation**: `hasAuthority('ADMIN')`

| Méthode | Endpoint       | Description                               | Requête Body | Réponse Body |
|---------|----------------|-------------------------------------------|--------------|--------------|
| `GET`   | `/`            | Récupère tous les utilisateurs.           | Aucun        | `Collection<UserDto>` |
| `GET`   | `/{id}`        | Récupère un utilisateur par son ID.       | Aucun        | `UserAdmin`  |
| `GET`   | `/roles`       | Récupère tous les rôles disponibles.      | Aucun        | `Collection<String>` |
| `PATCH` | `/{id}`        | Met à jour un utilisateur existant.       | `UserAdmin`  | `UserAdmin`  |

**Exemples JSON pour `AdminUserController`:**

#### `GET /admin/users`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "firstname": "John",
      "lastname": "Doe",
      "role": "ADMIN"
    },
    {
      "id": 2,
      "firstname": "Jane",
      "lastname": "Smith",
      "role": "CANDIDAT"
    }
  ]
  ```

#### `GET /admin/users/{id}`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "firstname": "John",
    "lastname": "Doe",
    "email": "john.doe@epf.fr",
    "password": null,
    "birthDate": "1990-01-15",
    "role": "ADMIN",
    "idRfid": "RFID12345"
  }
  ```
- **Réponse (404 Not Found)**:
  ```json
  "User not found with id: 99"
  ```

#### `GET /admin/users/roles`
- **Réponse (200 OK)**:
  ```json
  [
    "ADMIN",
    "CANDIDAT",
    "ETUDIANT",
    "ENSEIGNANT",
    "GESTIONNAIRE_ADMISSION"
  ]
  ```

#### `PATCH /admin/users/{id}`
- **Requête (Request Body)**:
  ```json
  {
    "firstname": "Jonathan",
    "role": "ENSEIGNANT"
  }
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "firstname": "Jonathan",
    "lastname": "Doe",
    "email": "john.doe@epf.fr",
    "password": null,
    "birthDate": "1990-01-15",
    "role": "ENSEIGNANT",
    "idRfid": "RFID12345"
  }
  ```
- **Réponse (400 Bad Request)**:
  ```json
  "Failed to update user: Invalid role provided"
  ```

### 1.2. `ImpersonateController`

- **Base URL**: `/admin`
- **Pré-autorisation**: `hasAuthority('ADMIN')` pour `/impersonate/{userId}`

| Méthode | Endpoint             | Description                               | Requête Body | Réponse Body |
|---------|----------------------|-------------------------------------------|--------------|--------------|
| `POST`  | `/impersonate/{userId}` | Permet à un administrateur d'usurper l'identité d'un autre utilisateur. | Aucun        | `Me`         |
| `POST`  | `/impersonate/revert` | Permet de revenir à l'identité de l'administrateur après une usurpation. | Aucun        | `Me`         |

**Exemples JSON pour `ImpersonateController`:**

#### `POST /admin/impersonate/{userId}`
- **Réponse (200 OK)**:
  ```json
  {
    "email": "jane.smith@epf.fr",
    "roles": ["CANDIDAT"]
  }
  ```
- **Réponse (404 Not Found)**:
  ```json
  "User not found with id: 99"
  ```

#### `POST /admin/impersonate/revert`
- **Réponse (200 OK)**:
  ```json
  {
    "email": "admin@epf.fr",
    "roles": ["ADMIN"]
  }
  ```
- **Réponse (400 Bad Request)**:
  ```json
  "No original admin context found in session"
  ```

---

## 2. Module `grades`

### 2.1. `NoteController`

- **Base URL**: `/api`

| Méthode | Endpoint                   | Description                               | Requête Body | Réponse Body |
|---------|----------------------------|-------------------------------------------|--------------|--------------|
| `POST`  | `/carnets`                 | Crée un nouveau carnet de notes.          | `Map<String, Object>` | `Map<String, Object>` (CarnetDeNotes) |
| `GET`   | `/carnets`                 | Récupère tous les carnets de notes.       | Aucun        | `List<Map<String, Object>>` |
| `GET`   | `/carnets/{id}`            | Récupère un carnet de notes par ID.       | Aucun        | `Map<String, Object>` (CarnetDeNotes) |
| `PATCH` | `/carnets/{id}/publier`    | Publie un carnet de notes.                | Aucun        | `Map<String, Object>` (CarnetDeNotes) |
| `POST`  | `/carnets/{carnetId}/evaluations` | Ajoute une évaluation à un carnet.        | `Map<String, Object>` | `Map<String, Object>` (Evaluation) |
| `GET`   | `/carnets/{carnetId}/evaluations` | Récupère les évaluations d'un carnet.     | Aucun        | `List<Map<String, Object>>` |
| `PUT`   | `/notes/saisir`            | Saisit ou met à jour une note.            | `Map<String, Object>` | `Map<String, Object>` (Note) |
| `POST`  | `/notes/import/{evaluationId}` | Importe des notes via un fichier CSV.     | `MultipartFile` | `Map<String, Object>` (résultat de l'import) |
| `GET`   | `/carnets/{carnetId}/moyennes` | Calcule et récupère les moyennes des étudiants pour un carnet. | Aucun        | `List<Map<String, Object>>` |

**Exemples JSON pour `NoteController`:**

#### `POST /api/carnets`
- **Requête (Request Body)**:
  ```json
  {
    "intitule": "Carnet de notes S1 2023-2024",
    "anneeAcademique": "2023-2024",
    "uniteEnseignementId": 1,
    "moduleId": 2
  }
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "intitule": "Carnet de notes S1 2023-2024",
    "anneeAcademique": "2023-2024",
    "statut": "BROUILLON",
    "moyenneClasse": null,
    "dateCreation": "2023-10-27T10:00:00",
    "datePublication": null,
    "uniteEnseignementId": 1,
    "uniteEnseignementIntitule": "Mathématiques",
    "nbEvaluations": 0
  }
  ```

#### `GET /api/carnets`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "intitule": "Carnet de notes S1 2023-2024",
      "anneeAcademique": "2023-2024",
      "statut": "BROUILLON",
      "moyenneClasse": null,
      "dateCreation": "2023-10-27T10:00:00",
      "datePublication": null,
      "uniteEnseignementId": 1,
      "uniteEnseignementIntitule": "Mathématiques",
      "nbEvaluations": 2
    }
  ]
  ```

#### `GET /api/carnets/{id}`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "intitule": "Carnet de notes S1 2023-2024",
    "anneeAcademique": "2023-2024",
    "statut": "BROUILLON",
    "moyenneClasse": null,
    "dateCreation": "2023-10-27T10:00:00",
    "datePublication": null,
    "uniteEnseignementId": 1,
    "uniteEnseignementIntitule": "Mathématiques",
    "nbEvaluations": 2
  }
  ```

#### `PATCH /api/carnets/{id}/publier`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "intitule": "Carnet de notes S1 2023-2024",
    "anneeAcademique": "2023-2024",
    "statut": "PUBLIE",
    "moyenneClasse": null,
    "dateCreation": "2023-10-27T10:00:00",
    "datePublication": "2023-10-27T15:30:00",
    "uniteEnseignementId": 1,
    "uniteEnseignementIntitule": "Mathématiques",
    "nbEvaluations": 2
  }
  ```

#### `POST /api/carnets/{carnetId}/evaluations`
- **Requête (Request Body)**:
  ```json
  {
    "intitule": "Examen Final",
    "type": "EXAMEN",
    "coef": 2.0,
    "noteMax": 20.0
  }
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "id": 101,
    "intitule": "Examen Final",
    "type": "EXAMEN",
    "coef": 2.0,
    "noteMax": 20.0,
    "dateEval": null,
    "carnetId": 1
  }
  ```

#### `GET /api/carnets/{carnetId}/evaluations`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 101,
      "intitule": "Examen Final",
      "type": "EXAMEN",
      "coef": 2.0,
      "noteMax": 20.0,
      "dateEval": null,
      "carnetId": 1
    }
  ]
  ```

#### `PUT /api/notes/saisir`
- **Requête (Request Body)**:
  ```json
  {
    "evaluationId": 101,
    "etudiantId": 501,
    "valeur": 15.5,
    "commentaire": "Bonne performance"
  }
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "id": 201,
    "valeurNote": 15.5,
    "absent": false,
    "commentaire": "Bonne performance",
    "source": "MANUELLE",
    "dateSaisie": "2023-10-27T16:00:00",
    "etudiantId": 501,
    "evaluationId": 101
  }
  ```

#### `POST /api/notes/import/{evaluationId}`
- **Requête (MultipartFile)**: Un fichier CSV avec le format `etudiant_numero,valeur_note,commentaire`
  ```csv
  123456,14.5,
  123457,ABS,Absent justifié
  123458,12,
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "imported": 2,
    "errors": 1,
    "errorDetails": [
      "Ligne 3 : étudiant introuvable → 123458"
    ]
  }
  ```

#### `GET /api/carnets/{carnetId}/moyennes`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "etudiantId": 501,
      "studentNumber": "EPF2023-001",
      "userId": 10,
      "campus": {
        "id": 1,
        "ville": "Cachan"
      },
      "moyenne": 14.25
    }
  ]
  ```

---

## 3. Module `student`

### 3.1. `StudentController`

- **Base URL**: `/api/students`

| Méthode | Endpoint       | Description                               | Pré-autorisation | Requête Body | Réponse Body |
|---------|----------------|-------------------------------------------|------------------|--------------|--------------|
| `GET`   | `/`            | Récupère tous les étudiants.              | `hasAuthority('ADMIN')` | Aucun        | `List<StudentDTO>` |
| `GET`   | `/{id}`        | Récupère un étudiant par ID.              | `hasAuthority('ADMIN')` | Aucun        | `StudentDTO` |
| `POST`  | `/`            | Crée un nouvel étudiant.                  | Aucune           | `Student`    | `Student`    |
| `PUT`   | `/{id}`        | Met à jour un étudiant existant.          | Aucune           | `Student`    | `Student`    |
| `DELETE`| `/{id}`        | Supprime un étudiant.                     | Aucune           | Aucun        | `Void`       |
| `GET`   | `/me`          | Récupère les informations de l'étudiant connecté. | `isAuthenticated()` | Aucun        | `StudentDTO` |

**Exemples JSON pour `StudentController`:**

#### `GET /api/students`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "studentNumber": "EPF2023-001",
      "user": {
        "id": 10,
        "firstname": "Alice",
        "lastname": "Dupont",
        "email": "alice.dupont@epf.fr",
        "birthDate": "2002-05-10"
      },
      "gender": "FEMALE",
      "nationality": "Française",
      "phone": "0612345678",
      "address": "10 Rue de la Paix, 75001 Paris",
      "academicYear": "2023-2024",
      "major": "Généraliste",
      "program": "Ingénieur",
      "campus": "Cachan",
      "scholarship": true,
      "lastDegree": "Bac S",
      "photoUrl": "http://example.com/photo1.jpg",
      "enrollmentDate": "2023-09-01"
    }
  ]
  ```

#### `GET /api/students/{id}`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "studentNumber": "EPF2023-001",
    "user": {
      "id": 10,
      "firstname": "Alice",
      "lastname": "Dupont",
      "email": "alice.dupont@epf.fr",
      "birthDate": "2002-05-10"
    },
    "gender": "FEMALE",
    "nationality": "Française",
    "phone": "0612345678",
    "address": "10 Rue de la Paix, 75001 Paris",
    "academicYear": "2023-2024",
    "major": "Généraliste",
    "program": "Ingénieur",
    "campus": "Cachan",
    "scholarship": true,
    "lastDegree": "Bac S",
    "photoUrl": "http://example.com/photo1.jpg",
    "enrollmentDate": "2023-09-01"
  }
  ```
- **Réponse (404 Not Found)**:
  ```json
  {
    "status": 404,
    "error": "Not Found",
    "message": "Student non trouvé avec Id : 99"
  }
  ```

#### `POST /api/students`
- **Requête (Request Body)**:
  ```json
  {
    "studentNumber": "EPF2023-002",
    "user": {
      "id": 11,
      "firstname": "Bob",
      "lastname": "Martin",
      "email": "bob.martin@epf.fr",
      "birthDate": "2001-11-20"
    },
    "gender": "MALE",
    "nationality": "Française",
    "phone": "0787654321",
    "address": "20 Avenue des Champs, 75008 Paris",
    "academicYear": "2023-2024",
    "major": "Généraliste",
    "program": "Ingénieur",
    "campus": {
      "id": 1,
      "ville": "Cachan"
    },
    "scholarship": false,
    "lastDegree": "Bac STI2D",
    "photoUrl": null,
    "enrollmentDate": "2023-09-01"
  }
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "id": 2,
    "studentNumber": "EPF2023-002",
    "user": {
      "id": 11,
      "firstname": "Bob",
      "lastname": "Martin",
      "email": "bob.martin@epf.fr",
      "birthDate": "2001-11-20",
      "role": "ETUDIANT"
    },
    "gender": "MALE",
    "nationality": "Française",
    "phone": "0787654321",
    "address": "20 Avenue des Champs, 75008 Paris",
    "academicYear": "2023-2024",
    "major": "Généraliste",
    "program": "Ingénieur",
    "campus": {
      "id": 1,
      "ville": "Cachan"
    },
    "scholarship": false,
    "lastDegree": "Bac STI2D",
    "photoUrl": null,
    "enrollmentDate": "2023-09-01",
    "createdAt": "2023-10-27T17:00:00"
  }
  ```

#### `PUT /api/students/{id}`
- **Requête (Request Body)**:
  ```json
  {
    "phone": "0600000000",
    "address": "Nouvelle Adresse, 75000 Paris"
  }
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "studentNumber": "EPF2023-001",
    "user": {
      "id": 10,
      "firstname": "Alice",
      "lastname": "Dupont",
      "email": "alice.dupont@epf.fr",
      "birthDate": "2002-05-10",
      "role": "ETUDIANT"
    },
    "gender": "FEMALE",
    "nationality": "Française",
    "phone": "0600000000",
    "address": "Nouvelle Adresse, 75000 Paris",
    "academicYear": "2023-2024",
    "major": "Généraliste",
    "program": "Ingénieur",
    "campus": {
      "id": 1,
      "ville": "Cachan"
    },
    "scholarship": true,
    "lastDegree": "Bac S",
    "photoUrl": "http://example.com/photo1.jpg",
    "enrollmentDate": "2023-09-01",
    "createdAt": "2023-10-27T17:00:00"
  }
  ```
- **Réponse (404 Not Found)**:
  ```json
  {}
  ```

#### `DELETE /api/students/{id}`
- **Réponse (204 No Content)**:
  ```
  (Pas de corps de réponse)
  ```

#### `GET /api/students/me`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "studentNumber": "EPF2023-001",
    "user": {
      "id": 10,
      "firstname": "Alice",
      "lastname": "Dupont",
      "email": "alice.dupont@epf.fr",
      "birthDate": "2002-05-10"
    },
    "gender": "FEMALE",
    "nationality": "Française",
    "phone": "0612345678",
    "address": "10 Rue de la Paix, 75001 Paris",
    "academicYear": "2023-2024",
    "major": "Généraliste",
    "program": "Ingénieur",
    "campus": "Cachan",
    "scholarship": true,
    "lastDegree": "Bac S",
    "photoUrl": "http://example.com/photo1.jpg",
    "enrollmentDate": "2023-09-01"
  }
  ```
- **Réponse (401 Unauthorized)**:
  ```json
  "Utilisateur non authentifié"
  ```

---

## 4. Module `documentStudent`

### 4.1. `DocumentController`

- **Base URL**: `/api/document_student`

| Méthode | Endpoint                   | Description                               | Pré-autorisation | Requête Body | Réponse Body |
|---------|----------------------------|-------------------------------------------|------------------|--------------|--------------|
| `GET`   | `/`                        | Récupère tous les documents étudiants.    | Aucune           | Aucun        | `List<DocumentStudent>` |
| `GET`   | `/{id}`                    | Récupère un document étudiant par ID.     | Aucune           | Aucun        | `DocumentStudent` |
| `GET`   | `/user/{userId}`           | Récupère les documents d'un utilisateur.  | Aucune           | Aucun        | `List<DocumentStudent>` |
| `POST`  | `/`                        | Crée un nouveau document étudiant.        | Aucune           | `DocumentStudent` | `DocumentStudent` |
| `PUT`   | `/{id}`                    | Met à jour un document étudiant.          | Aucune           | `DocumentStudent` | `DocumentStudent` |
| `DELETE`| `/{id}`                    | Supprime un document étudiant.            | Aucune           | Aucun        | `Void`       |
| `GET`   | `/generate/certificate/{studentId}` | Génère un certificat de scolarité en PDF. | Aucune           | Aucun        | `byte[]` (PDF) |
| `GET`   | `/generate/pdf/{studentId}` | Génère un PDF d'informations personnelles. | Aucune           | Aucun        | `byte[]` (PDF) |
| `PATCH` | `/{id}/archive`            | Archive un document étudiant.             | `hasAuthority('ADMIN')` | Aucun (`archivedBy` en param) | `DocumentStudent` |
| `GET`   | `/user/{userId}/archived`  | Récupère les documents archivés d'un utilisateur. | `hasAuthority('ADMIN')` | Aucun        | `List<DocumentStudentDTO>` |
| `PATCH` | `/{id}/unarchive`          | Désarchive un document étudiant.          | `hasAuthority('ADMIN')` | Aucun        | `DocumentStudent` |
| `GET`   | `/archived`                | Récupère tous les documents archivés.     | `hasAuthority('ADMIN')` | Aucun        | `List<DocumentStudentDTO>` |

**Exemples JSON pour `DocumentController`:**

#### `GET /api/document_student`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "userId": 10,
      "documentType": "CERTIFICATE_SCOLAR",
      "status": "APPROVED",
      "creationDate": "2023-10-27T10:00:00",
      "processingDate": "2023-10-27T10:05:00",
      "fileUrl": "base64encodedstring...",
      "archivedAt": null,
      "archivedBy": null
    }
  ]
  ```

#### `GET /api/document_student/{id}`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "userId": 10,
    "documentType": "CERTIFICATE_SCOLAR",
    "status": "APPROVED",
    "creationDate": "2023-10-27T10:00:00",
    "processingDate": "2023-10-27T10:05:00",
    "fileUrl": "base64encodedstring...",
    "archivedAt": null,
    "archivedBy": null
  }
  ```
- **Réponse (404 Not Found)**:
  ```json
  "Document not found with id: 99"
  ```

#### `GET /api/document_student/user/{userId}`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "userId": 10,
      "documentType": "CERTIFICATE_SCOLAR",
      "status": "APPROVED",
      "creationDate": "2023-10-27T10:00:00",
      "processingDate": "2023-10-27T10:05:00",
      "fileUrl": "base64encodedstring...",
      "archivedAt": null,
      "archivedBy": null
    }
  ]
  ```

#### `POST /api/document_student`
- **Requête (Request Body)**:
  ```json
  {
    "userId": 10,
    "documentType": "INFOS_STUDENT",
    "status": "PENDING",
    "fileUrl": "base64encodedstring..."
  }
  ```
- **Réponse (201 Created)**:
  ```json
  {
    "id": 2,
    "userId": 10,
    "documentType": "INFOS_STUDENT",
    "status": "PENDING",
    "creationDate": "2023-10-27T10:30:00",
    "processingDate": null,
    "fileUrl": "base64encodedstring...",
    "archivedAt": null,
    "archivedBy": null
  }
  ```

#### `PUT /api/document_student/{id}`
- **Requête (Request Body)**:
  ```json
  {
    "documentType": "CERTIFICATE_SCOLAR",
    "status": "APPROVED"
  }
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "userId": 10,
    "documentType": "CERTIFICATE_SCOLAR",
    "status": "APPROVED",
    "creationDate": "2023-10-27T10:00:00",
    "processingDate": "2023-10-27T10:05:00",
    "fileUrl": "base64encodedstring...",
    "archivedAt": null,
    "archivedBy": null
  }
  ```
- **Réponse (403 Forbidden)**:
  ```json
  {
    "status": 403,
    "error": "Forbidden",
    "message": "Ce document est archivé et ne peut plus être modifié."
  }
  ```

#### `DELETE /api/document_student/{id}`
- **Réponse (204 No Content)**:
  ```
  (Pas de corps de réponse)
  ```
- **Réponse (403 Forbidden)**:
  ```json
  {
    "status": 403,
    "error": "Forbidden",
    "message": "Ce document est archivé et ne peut pas être supprimé."
  }
  ```

#### `GET /api/document_student/generate/certificate/{studentId}`
- **Réponse (200 OK)**:
  ```
  (Contenu binaire du PDF)
  ```

#### `GET /api/document_student/generate/pdf/{studentId}`
- **Réponse (200 OK)**:
  ```
  (Contenu binaire du PDF)
  ```

#### `PATCH /api/document_student/{id}/archive`
- **Requête (Query Parameter)**: `archivedBy=adminUser`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "userId": 10,
    "documentType": "CERTIFICATE_SCOLAR",
    "status": "ARCHIVED",
    "creationDate": "2023-10-27T10:00:00",
    "processingDate": "2023-10-27T10:05:00",
    "fileUrl": "base64encodedstring...",
    "archivedAt": "2023-10-27T11:00:00",
    "archivedBy": "adminUser"
  }
  ```
- **Réponse (409 Conflict)**:
  ```json
  {
    "status": 409,
    "error": "Conflict",
    "message": "Ce document est déjà archivé."
  }
  ```

#### `GET /api/document_student/user/{userId}/archived`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "documentType": "CERTIFICATE_SCOLAR",
      "status": "ARCHIVED",
      "creationDate": "2023-10-27T10:00:00",
      "processingDate": "2023-10-27T10:05:00",
      "archivedAt": "2023-10-27T11:00:00",
      "archivedBy": "adminUser",
      "userId": 10,
      "studentId": 1,
      "firstname": "Alice",
      "lastname": "Dupont"
    }
  ]
  ```

#### `PATCH /api/document_student/{id}/unarchive`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "userId": 10,
    "documentType": "CERTIFICATE_SCOLAR",
    "status": "APPROVED",
    "creationDate": "2023-10-27T10:00:00",
    "processingDate": "2023-10-27T10:05:00",
    "fileUrl": "base64encodedstring...",
    "archivedAt": null,
    "archivedBy": null
  }
  ```

#### `GET /api/document_student/archived`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "documentType": "CERTIFICATE_SCOLAR",
      "status": "ARCHIVED",
      "creationDate": "2023-10-27T10:00:00",
      "processingDate": "2023-10-27T10:05:00",
      "archivedAt": "2023-10-27T11:00:00",
      "archivedBy": "adminUser",
      "userId": 10,
      "studentId": 1,
      "firstname": "Alice",
      "lastname": "Dupont"
    }
  ]
  ```

---

## 5. Module `documentFormulaire`

### 5.1. `DocumentFormulaireController`

- **Base URL**: `/formulaire/{formulaireId}/documents`

| Méthode | Endpoint             | Description                               | Pré-autorisation | Requête Body | Réponse Body |
|---------|----------------------|-------------------------------------------|------------------|--------------|--------------|
| `POST`  | `/{documentType}`    | Télécharge un document pour un formulaire. | Aucune           | `MultipartFile` | `DocumentFormulaireDTO` |
| `GET`   | `/`                  | Récupère tous les documents d'un formulaire. | Aucune           | Aucun        | `List<DocumentFormulaireDTO>` |
| `GET`   | `/{documentType}`    | Télécharge un document spécifique d'un formulaire. | Aucune           | Aucun        | `Resource`   |
| `DELETE`| `/{documentType}`    | Supprime un document d'un formulaire.     | Aucune           | Aucun        | `Void`       |

**Exemples JSON pour `DocumentFormulaireController`:**

#### `POST /formulaire/{formulaireId}/documents/{documentType}`
- **Requête (MultipartFile)**: Un fichier à téléverser.
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "formulaireId": 101,
    "documentType": "CV",
    "fileUrl": "101_CV.pdf",
    "fileName": "mon_cv.pdf"
  }
  ```
- **Réponse (404 Not Found)**:
  ```json
  {
    "status": 404,
    "error": "Not Found",
    "message": "Formulaire not found"
  }
  ```

#### `GET /formulaire/{formulaireId}/documents`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "formulaireId": 101,
      "documentType": "CV",
      "fileUrl": "101_CV.pdf",
      "fileName": "mon_cv.pdf"
    },
    {
      "id": 2,
      "formulaireId": 101,
      "documentType": "LETTRE_MOTIVATION",
      "fileUrl": "101_LETTRE_MOTIVATION.pdf",
      "fileName": "ma_lettre.pdf"
    }
  ]
  ```

#### `GET /formulaire/{formulaireId}/documents/{documentType}`
- **Réponse (200 OK)**:
  ```
  (Contenu binaire du fichier)
  ```
- **Réponse (404 Not Found)**:
  ```json
  {
    "status": 404,
    "error": "Not Found",
    "message": "Document not found"
  }
  ```

#### `DELETE /formulaire/{formulaireId}/documents/{documentType}`
- **Réponse (204 No Content)**:
  ```
  (Pas de corps de réponse)
  ```
- **Réponse (404 Not Found)**:
  ```json
  {
    "status": 404,
    "error": "Not Found",
    "message": "Document not found"
  }
  ```

---

## 6. Module `entretienCandidature`

### 6.1. `EntretienCandidatureController`

- **Base URL**: `/entretien`
- **Pré-autorisation**: `hasAuthority('GESTIONNAIRE_ADMISSION')` pour la plupart des endpoints

| Méthode | Endpoint             | Description                               | Pré-autorisation | Requête Body | Réponse Body |
|---------|----------------------|-------------------------------------------|------------------|--------------|--------------|
| `POST`  | `/`                  | Crée un nouvel entretien de candidature.  | `hasAuthority('GESTIONNAIRE_ADMISSION')` | `EntretienCandidatureDTO` | `EntretienCandidatureDTO` |
| `PUT`   | `/{id}`              | Met à jour un entretien de candidature.   | `hasAuthority('GESTIONNAIRE_ADMISSION')` | `EntretienCandidatureDTO` | `EntretienCandidatureDTO` |
| `GET`   | `/mes-entretiens`    | Récupère les entretiens de l'utilisateur connecté. | `isAuthenticated()` | Aucun        | `List<EntretienCandidatureDTO>` |
| `GET`   | `/{id}`              | Récupère un entretien par ID.             | `hasAuthority('GESTIONNAIRE_ADMISSION')` | Aucun        | `EntretienCandidatureDTO` |
| `GET`   | `/interviewers`      | Récupère la liste des interviewers.       | `hasAuthority('GESTIONNAIRE_ADMISSION')` | Aucun        | `List<InterviewerDTO>` |
| `GET`   | `/formulaire/{formulaireId}` | Récupère les entretiens liés à un formulaire. | `hasAuthority('GESTIONNAIRE_ADMISSION')` | Aucun        | `List<EntretienCandidatureDTO>` |
| `GET`   | `/`                  | Récupère tous les entretiens.             | `hasAuthority('GESTIONNAIRE_ADMISSION')` | Aucun        | `List<EntretienCandidatureDTO>` |
| `DELETE`| `/{id}`              | Supprime un entretien.                    | `hasAuthority('GESTIONNAIRE_ADMISSION')` | Aucun        | `Void`       |

**Exemples JSON pour `EntretienCandidatureController`:**

#### `POST /entretien`
- **Requête (Request Body)**:
  ```json
  {
    "formulaireId": 1,
    "interviewerId": 5,
    "dateHeure": "2024-01-15T10:00:00",
    "campusVille": "Cachan",
    "salle": "B201",
    "typeEntretien": "PRESENTIEL",
    "statut": "PLANIFIE"
  }
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "formulaireId": 1,
    "candidat": {
      "firstname": "Alice",
      "lastname": "Dupont",
      "email": "alice.dupont@epf.fr",
      "birthDate": "2002-05-10",
      "role": "CANDIDAT"
    },
    "interviewerId": 5,
    "interviewer": {
      "firstname": "Prof",
      "lastname": "esseur",
      "email": "prof.esseur@epf.fr",
      "birthDate": "1975-03-20",
      "role": "ENSEIGNANT"
    },
    "dateHeure": "2024-01-15T10:00:00",
    "campusVille": "Cachan",
    "salle": "B201",
    "lienVisio": null,
    "typeEntretien": "PRESENTIEL",
    "statut": "PLANIFIE",
    "note": null,
    "commentaire": null,
    "dateCreation": "2023-10-27T18:00:00",
    "decisionAdmission": "EN_ATTENTE"
  }
  ```

#### `PUT /entretien/{id}`
- **Requête (Request Body)**:
  ```json
  {
    "formulaireId": 1,
    "interviewerId": 5,
    "dateHeure": "2024-01-15T11:00:00",
    "campusVille": "Cachan",
    "salle": "B202",
    "typeEntretien": "PRESENTIEL",
    "statut": "TERMINE",
    "note": 15.5,
    "commentaire": "Très bon entretien"
  }
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "formulaireId": 1,
    "candidat": {
      "firstname": "Alice",
      "lastname": "Dupont",
      "email": "alice.dupont@epf.fr",
      "birthDate": "2002-05-10",
      "role": "CANDIDAT"
    },
    "interviewerId": 5,
    "interviewer": {
      "firstname": "Prof",
      "lastname": "esseur",
      "email": "prof.esseur@epf.fr",
      "birthDate": "1975-03-20",
      "role": "ENSEIGNANT"
    },
    "dateHeure": "2024-01-15T11:00:00",
    "campusVille": "Cachan",
    "salle": "B202",
    "lienVisio": null,
    "typeEntretien": "PRESENTIEL",
    "statut": "TERMINE",
    "note": 15.5,
    "commentaire": "Très bon entretien",
    "dateCreation": "2023-10-27T18:00:00",
    "decisionAdmission": "EN_ATTENTE"
  }
  ```

#### `GET /entretien/mes-entretiens`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "formulaireId": 1,
      "candidat": {
        "firstname": "Alice",
        "lastname": "Dupont",
        "email": "alice.dupont@epf.fr",
        "birthDate": "2002-05-10",
        "role": "CANDIDAT"
      },
      "interviewerId": 5,
      "interviewer": {
        "firstname": "Prof",
        "lastname": "esseur",
        "email": "prof.esseur@epf.fr",
        "birthDate": "1975-03-20",
        "role": "ENSEIGNANT"
      },
      "dateHeure": "2024-01-15T10:00:00",
      "campusVille": "Cachan",
      "salle": "B201",
      "lienVisio": null,
      "typeEntretien": "PRESENTIEL",
      "statut": "PLANIFIE",
      "note": null,
      "commentaire": null,
      "dateCreation": "2023-10-27T18:00:00",
      "decisionAdmission": "EN_ATTENTE"
    }
  ]
  ```

#### `GET /entretien/{id}`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "formulaireId": 1,
    "candidat": {
      "firstname": "Alice",
      "lastname": "Dupont",
      "email": "alice.dupont@epf.fr",
      "birthDate": "2002-05-10",
      "role": "CANDIDAT"
    },
    "interviewerId": 5,
    "interviewer": {
      "firstname": "Prof",
      "lastname": "esseur",
      "email": "prof.esseur@epf.fr",
      "birthDate": "1975-03-20",
      "role": "ENSEIGNANT"
    },
    "dateHeure": "2024-01-15T10:00:00",
    "campusVille": "Cachan",
    "salle": "B201",
    "lienVisio": null,
    "typeEntretien": "PRESENTIEL",
    "statut": "PLANIFIE",
    "note": null,
    "commentaire": null,
    "dateCreation": "2023-10-27T18:00:00",
    "decisionAdmission": "EN_ATTENTE"
  }
  ```

#### `GET /entretien/interviewers`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 5,
      "firstname": "Prof",
      "lastname": "esseur",
      "email": "prof.esseur@epf.fr"
    },
    {
      "id": 6,
      "firstname": "Gest",
      "lastname": "ionnaire",
      "email": "gest.ionnaire@epf.fr"
    }
  ]
  ```

#### `GET /entretien/formulaire/{formulaireId}`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "formulaireId": 1,
      "candidat": {
        "firstname": "Alice",
        "lastname": "Dupont",
        "email": "alice.dupont@epf.fr",
        "birthDate": "2002-05-10",
        "role": "CANDIDAT"
      },
      "interviewerId": 5,
      "interviewer": {
        "firstname": "Prof",
        "lastname": "esseur",
        "email": "prof.esseur@epf.fr",
        "birthDate": "1975-03-20",
        "role": "ENSEIGNANT"
      },
      "dateHeure": "2024-01-15T10:00:00",
      "campusVille": "Cachan",
      "salle": "B201",
      "lienVisio": null,
      "typeEntretien": "PRESENTIEL",
      "statut": "PLANIFIE",
      "note": null,
      "commentaire": null,
      "dateCreation": "2023-10-27T18:00:00",
      "decisionAdmission": "EN_ATTENTE"
    }
  ]
  ```

#### `GET /entretien`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "formulaireId": 1,
      "candidat": {
        "firstname": "Alice",
        "lastname": "Dupont",
        "email": "alice.dupont@epf.fr",
        "birthDate": "2002-05-10",
        "role": "CANDIDAT"
      },
      "interviewerId": 5,
      "interviewer": {
        "firstname": "Prof",
        "lastname": "esseur",
        "email": "prof.esseur@epf.fr",
        "birthDate": "1975-03-20",
        "role": "ENSEIGNANT"
      },
      "dateHeure": "2024-01-15T10:00:00",
      "campusVille": "Cachan",
      "salle": "B201",
      "lienVisio": null,
      "typeEntretien": "PRESENTIEL",
      "statut": "PLANIFIE",
      "note": null,
      "commentaire": null,
      "dateCreation": "2023-10-27T18:00:00",
      "decisionAdmission": "EN_ATTENTE"
    }
  ]
  ```

#### `DELETE /entretien/{id}`
- **Réponse (204 No Content)**:
  ```
  (Pas de corps de réponse)
  ```

---

## 7. Module `formulaireInscription`

### 7.1. `FormulaireInscriptionController`

- **Base URL**: `/formulaire`

| Méthode | Endpoint             | Description                               | Pré-autorisation | Requête Body | Réponse Body |
|---------|----------------------|-------------------------------------------|------------------|--------------|--------------|
| `POST`  | `/`                  | Sauvegarde un formulaire d'inscription.   | Aucune           | `FormulaireInscriptionDTO` | `FormulaireInscriptionDTO` |
| `PUT`   | `/`                  | Met à jour un formulaire d'inscription.   | Aucune           | `FormulaireInscriptionDTO` | `FormulaireInscriptionDTO` |
| `GET`   | `/candidatform`      | Récupère le formulaire de l'utilisateur connecté. | Aucune           | Aucun        | `FormulaireInscriptionDTO` |
| `GET`   | `/{id}`              | Récupère un formulaire par ID.            | `hasAuthority('GESTIONNAIRE_ADMISSION')` | Aucun        | `FormulaireInscriptionDTO` |
| `GET`   | `/soumis`            | Récupère tous les formulaires soumis en attente de décision. | `hasAuthority('GESTIONNAIRE_ADMISSION')` | Aucun        | `List<FormulaireInscriptionDTO>` |
| `GET`   | `/`                  | Récupère tous les formulaires.            | `hasAuthority('GESTIONNAIRE_ADMISSION')` | Aucun        | `List<FormulaireInscriptionDTO>` |
| `PATCH` | `/{id}/decision`     | Met à jour la décision d'admission d'un formulaire. | `hasAuthority('GESTIONNAIRE_ADMISSION')` | Aucun (`decision` en param) | `FormulaireInscriptionDTO` |
| `DELETE`| `/{id}`              | Supprime un formulaire.                   | `hasAuthority('GESTIONNAIRE_ADMISSION')` | Aucun        | `Void`       |

**Exemples JSON pour `FormulaireInscriptionController`:**

#### `POST /formulaire`
- **Requête (Request Body)**:
  ```json
  {
    "dernierDiplome": "Bac S",
    "etablissement": "Lycée Henri IV",
    "niveauEtude": "Terminal",
    "anneeObtention": 2023,
    "programmeChoisi": "Ingénieur Généraliste",
    "campusVille": "Cachan",
    "soumis": true,
    "genre": "FEMME",
    "telephone": "0612345678",
    "nationalite": "Française",
    "adresse": "10 Rue de la Paix, 75001 Paris",
    "anneeIntegration": 1,
    "majeur": "Informatique"
  }
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "user": {
      "firstname": "Alice",
      "lastname": "Dupont",
      "email": "alice.dupont@epf.fr",
      "birthDate": "2002-05-10",
      "role": "CANDIDAT"
    },
    "dernierDiplome": "Bac S",
    "etablissement": "Lycée Henri IV",
    "niveauEtude": "Terminal",
    "anneeObtention": 2023,
    "programmeChoisi": "Ingénieur Généraliste",
    "dateSoumission": "2023-10-27T19:00:00",
    "campusVille": "Cachan",
    "soumis": true,
    "genre": "FEMME",
    "telephone": "0612345678",
    "nationalite": "Française",
    "adresse": "10 Rue de la Paix, 75001 Paris",
    "anneeIntegration": 1,
    "majeur": "Informatique",
    "decisionAdmission": "EN_ATTENTE"
  }
  ```

#### `PUT /formulaire`
- **Requête (Request Body)**:
  ```json
  {
    "dernierDiplome": "Bac S",
    "etablissement": "Lycée Henri IV",
    "niveauEtude": "Terminal",
    "anneeObtention": 2023,
    "programmeChoisi": "Ingénieur Généraliste",
    "campusVille": "Cachan",
    "soumis": true,
    "genre": "FEMME",
    "telephone": "0600000000",
    "nationalite": "Française",
    "adresse": "Nouvelle Adresse, 75000 Paris",
    "anneeIntegration": 1,
    "majeur": "Informatique"
  }
  ```
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "user": {
      "firstname": "Alice",
      "lastname": "Dupont",
      "email": "alice.dupont@epf.fr",
      "birthDate": "2002-05-10",
      "role": "CANDIDAT"
    },
    "dernierDiplome": "Bac S",
    "etablissement": "Lycée Henri IV",
    "niveauEtude": "Terminal",
    "anneeObtention": 2023,
    "programmeChoisi": "Ingénieur Généraliste",
    "dateSoumission": "2023-10-27T19:00:00",
    "campusVille": "Cachan",
    "soumis": true,
    "genre": "FEMME",
    "telephone": "0600000000",
    "nationalite": "Française",
    "adresse": "Nouvelle Adresse, 75000 Paris",
    "anneeIntegration": 1,
    "majeur": "Informatique",
    "decisionAdmission": "EN_ATTENTE"
  }
  ```

#### `GET /formulaire/candidatform`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "user": {
      "firstname": "Alice",
      "lastname": "Dupont",
      "email": "alice.dupont@epf.fr",
      "birthDate": "2002-05-10",
      "role": "CANDIDAT"
    },
    "dernierDiplome": "Bac S",
    "etablissement": "Lycée Henri IV",
    "niveauEtude": "Terminal",
    "anneeObtention": 2023,
    "programmeChoisi": "Ingénieur Généraliste",
    "dateSoumission": "2023-10-27T19:00:00",
    "campusVille": "Cachan",
    "soumis": true,
    "genre": "FEMME",
    "telephone": "0612345678",
    "nationalite": "Française",
    "adresse": "10 Rue de la Paix, 75001 Paris",
    "anneeIntegration": 1,
    "majeur": "Informatique",
    "decisionAdmission": "EN_ATTENTE"
  }
  ```

#### `GET /formulaire/{id}`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "user": {
      "firstname": "Alice",
      "lastname": "Dupont",
      "email": "alice.dupont@epf.fr",
      "birthDate": "2002-05-10",
      "role": "CANDIDAT"
    },
    "dernierDiplome": "Bac S",
    "etablissement": "Lycée Henri IV",
    "niveauEtude": "Terminal",
    "anneeObtention": 2023,
    "programmeChoisi": "Ingénieur Généraliste",
    "dateSoumission": "2023-10-27T19:00:00",
    "campusVille": "Cachan",
    "soumis": true,
    "genre": "FEMME",
    "telephone": "0612345678",
    "nationalite": "Française",
    "adresse": "10 Rue de la Paix, 75001 Paris",
    "anneeIntegration": 1,
    "majeur": "Informatique",
    "decisionAdmission": "EN_ATTENTE"
  }
  ```

#### `GET /formulaire/soumis`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "user": {
        "firstname": "Alice",
        "lastname": "Dupont",
        "email": "alice.dupont@epf.fr",
        "birthDate": "2002-05-10",
        "role": "CANDIDAT"
      },
      "dernierDiplome": "Bac S",
      "etablissement": "Lycée Henri IV",
      "niveauEtude": "Terminal",
      "anneeObtention": 2023,
      "programmeChoisi": "Ingénieur Généraliste",
      "dateSoumission": "2023-10-27T19:00:00",
      "campusVille": "Cachan",
      "soumis": true,
      "genre": "FEMME",
      "telephone": "0612345678",
      "nationalite": "Française",
      "adresse": "10 Rue de la Paix, 75001 Paris",
      "anneeIntegration": 1,
      "majeur": "Informatique",
      "decisionAdmission": "EN_ATTENTE"
    }
  ]
  ```

#### `GET /formulaire`
- **Réponse (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "user": {
        "firstname": "Alice",
        "lastname": "Dupont",
        "email": "alice.dupont@epf.fr",
        "birthDate": "2002-05-10",
        "role": "CANDIDAT"
      },
      "dernierDiplome": "Bac S",
      "etablissement": "Lycée Henri IV",
      "niveauEtude": "Terminal",
      "anneeObtention": 2023,
      "programmeChoisi": "Ingénieur Généraliste",
      "dateSoumission": "2023-10-27T19:00:00",
      "campusVille": "Cachan",
      "soumis": true,
      "genre": "FEMME",
      "telephone": "0612345678",
      "nationalite": "Française",
      "adresse": "10 Rue de la Paix, 75001 Paris",
      "anneeIntegration": 1,
      "majeur": "Informatique",
      "decisionAdmission": "EN_ATTENTE"
    }
  ]
  ```

#### `PATCH /formulaire/{id}/decision`
- **Requête (Query Parameter)**: `decision=ADMIS`
- **Réponse (200 OK)**:
  ```json
  {
    "id": 1,
    "user": {
      "firstname": "Alice",
      "lastname": "Dupont",
      "email": "alice.dupont@epf.fr",
      "birthDate": "2002-05-10",
      "role": "ETUDIANT"
    },
    "dernierDiplome": "Bac S",
    "etablissement": "Lycée Henri IV",
    "niveauEtude": "Terminal",
    "anneeObtention": 2023,
    "programmeChoisi": "Ingénieur Généraliste",
    "dateSoumission": "2023-10-27T19:00:00",
    "campusVille": "Cachan",
    "soumis": true,
    "genre": "FEMME",
    "telephone": "0612345678",
    "nationalite": "Française",
    "adresse": "10 Rue de la Paix, 75001 Paris",
    "anneeIntegration": 1,
    "majeur": "Informatique",
    "decisionAdmission": "ADMIS"
  }
  ```

#### `DELETE /formulaire/{id}`
- **Réponse (204 No Content)**:
  ```
  (Pas de corps de réponse)
  ```
