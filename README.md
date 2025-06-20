
# Auth API Spring Boot + JWT

Ce projet est une API REST sécurisée construite avec Spring Boot, Spring Security et JWT. Il permet un système d'authentification simple (inscription/connexion), ainsi que la gestion de locations (`rentals`) liées à des utilisateurs.

## Fonctionnalités

- Inscription et connexion d’utilisateurs avec retour d’un token JWT
- Endpoints protégés nécessitant un token
- Récupération de l'utilisateur connecté (`/me`)
- Création, consultation, modification de locations
- Association automatique des locations à l'utilisateur connecté
- Upload d’image pour une location
- Documentation Swagger pour tester l’API

## Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT (via `io.jsonwebtoken`)
- Spring Data JPA + MySQL
- Swagger (Springdoc OpenAPI)
- Visual Studio Code

## Endpoints

| Méthode | URL                   | Accès     | Description                                  |
|---------|------------------------|-----------|----------------------------------------------|
| POST    | /auth/register         | Public    | Enregistre un nouvel utilisateur             |
| POST    | /auth/login            | Public    | Authentifie et retourne un JWT               |
| GET     | /me                    | Protégé   | Renvoie l'utilisateur actuellement connecté  |
| GET     | /api/rentals           | Protégé   | Liste toutes les locations                   |
| POST    | /api/rentals           | Protégé   | Crée une nouvelle location                   |
| GET     | /api/rentals/{id}      | Protégé   | Récupère une location spécifique             |
| PUT     | /api/rentals/{id}      | Protégé   | Met à jour une location existante            |
| GET     | /api/user/{id}         | Protégé   | Récupère les infos publiques d’un utilisateur|

### Exemple de header d'authentification

```
Authorization: Bearer <votre_token_jwt>
```

## Tester l'API

- Via Postman
- Ou via le front Angular associé
- Ou directement dans Swagger UI :  
  http://localhost:3001/swagger-ui/index.html

## Démarrage de l'application

**Forker le projet** depuis le dépôt GitHub.

**Cloner le projet** en local avec Git Desktop ou via la ligne de commande.

**Ouvrir le projet** avec Visual Studio Code.

**Installer les dépendances** en lançant la commande Maven suivante dans le terminal de VS Code :

```bash
./mvnw clean install
```

**Lancement de l'application**

```bash
./mvnw spring-boot:run
```

L'application sera accessible ensuite à l'URL :
http://localhost:3001/api/


## Base de données

Assurez-vous que votre base de données MySQL tourne et que le fichier `application.properties` contient les bons identifiants de connexion.

## Historique

Ce projet a été conçu pour explorer la sécurisation d'API REST avec Spring Boot, JWT, et une architecture MVC propre. Il a évolué pour inclure une gestion complète des ressources de location ainsi qu’une documentation interactive via Swagger.
