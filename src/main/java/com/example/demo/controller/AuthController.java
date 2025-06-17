package com.example.demo.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.JwtUtil;


import org.springframework.security.core.Authentication;
import com.example.demo.model.AuthRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
  


     @Autowired
    private AuthenticationManager authenticationManager;

     @Autowired
    private JwtUtil jwtUtil;  // ta classe utilitaire pour créer/valider le JWT

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
           System.out.println("Login attempt with email: " + authRequest.getEmail() + " and -->password<--: " + authRequest.getPassword());
       try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()
                )
            );

            System.out.println("Etape 11 : ");
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
             System.out.println("Etape 22 userDetails : "+userDetails);
            User user = ((CustomUserDetails) userDetails).getUser();
             System.out.println("Etape 33 user : "+ user);
            // Génération du token JWT
            String jwt = jwtUtil.generateToken(user);
            System.out.println("JWT généré : " + jwt);
            // Retourne le token dans la réponse JSON
            return ResponseEntity.ok(Map.of("token", jwt));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Identifiants invalides"+ex.getMessage()));
        }
    }

@PostMapping("/register")
public ResponseEntity<?> register(@RequestBody AuthRequest request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        return ResponseEntity.badRequest().body("Email déjà utilisé");
    }

    User user = new User();
    user.setEmail(request.getEmail());
    user.setName(request.getName());
    user.setPassword(passwordEncoder.encode(request.getPassword()));


try{
     userRepository.save(user);
   } catch (DataIntegrityViolationException dive) {
        System.out.println("Erreur DataIntegrityViolationException : " + dive.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Erreur d'intégrité des données : " + dive.getMostSpecificCause().getMessage());
    } catch (ConstraintViolationException cve) {
        System.out.println("Erreur ConstraintViolationException : " + cve.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Erreur de contrainte : " + cve.getMessage());
    } catch (Exception ex) {
        System.out.println("Erreur Exception générale : " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Erreur interne : " + ex.getMessage());
    }
   

       String token = jwtUtil.generateToken(user);

    // Retourner le token dans un objet JSON avec la clé "token"
    Map<String, String> response = new HashMap<>();
    response.put("token", token);

    return ResponseEntity.ok(response);
}

 @GetMapping("/me")
public ResponseEntity<?> getCurrentUser(Authentication authentication) {
      try {
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

   String email = customUserDetails.getEmail();
        System.out.println("Email extrait du token : '" + email + "'");
        System.out.println("Longueur email : " + email.length());

        if (email == null || email.isEmpty()) {
            System.out.println("Email est null ou vide");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email non fourni");
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            System.out.println("Aucun utilisateur trouvé avec cet email : " + email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        }

    return ResponseEntity.ok(Map.of(
        "id", user.getId(),
        "email", user.getEmail(),
        "name", user.getName(),
        "created_at",user.getCreatedAt(),
        "updated_at", user.getUpdatedAt() != null ? user.getUpdatedAt() : ""
    ));
      } catch (UsernameNotFoundException e) {
        System.out.println("Erreur : " + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
        System.out.println("Erreur inattendue : " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erreur serveur"));
    }
}
    }

   


