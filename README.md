# 🔐 Auth API Spring Boot + JWT

Ce projet est une API REST sécurisée avec Spring Boot, Spring Security et JWT. Elle fournit un système d'authentification simple (login/register) et des endpoints protégés accessibles uniquement avec un token valide.

## ⚙️ Fonctionnalités

- Enregistrement d'utilisateurs
- Connexion avec retour d’un token JWT
- Récupération de l'utilisateur courant (`/me`)
- Sécurisation des routes via JWT
- Gestion des erreurs (401, etc.)
- `created_at` et `updated_at` dans les entités

## 🧱 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT (via io.jsonwebtoken)
- MySQL 
- VS Code



## 🔐 Endpoints

| Méthode | URL           | Accès     | Description                  |
|---------|---------------|-----------|------------------------------|
| POST    | /auth/register| Public    | Enregistre un nouvel utilisateur |
| POST    | /auth/login   | Public    | Authentifie et retourne un JWT |
| GET     | /me           | Protégé   | Renvoie l'utilisateur courant |

### Exemple de header

```
Authorization: Bearer <votre_token>
```

## 🧪 Teste de l'API

- **Postman**
- Ou un front Angular (déjà intégré dans ce projet)

## 🗃️ Lancement

### 1. FORKER le projet deouis GIT


### 2. Compile et démarre

./mvnw spring-boot:run

Ou depuis VS Code via le plugin Spring Boot.




## 📅 Historique

Ce projet a été réalisé dans le cadre d'une montée en compétence sur les API sécurisées avec Spring Boot.

