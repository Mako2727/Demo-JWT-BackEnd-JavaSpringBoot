package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Map;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/auth")
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
            return ResponseEntity.ok(Map.of("jwt", jwt));
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

    userRepository.save(user);

    return ResponseEntity.ok("Utilisateur enregistré avec succès !");
}

 @GetMapping("/me")
public ResponseEntity<?> getCurrentUser(Authentication authentication) {
      CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    String email = customUserDetails.getEmail();
    System.out.println("mon email :" + email);

    User user = userRepository.findByEmail(email)
                  .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

    return ResponseEntity.ok(Map.of(
        "id", user.getId(),
        "email", user.getEmail(),
        "name", user.getName(),
        "created_at",user.getCreatedAt(),
        "updated_at",user.getUpdatedAt()


    ));
}
    }

   


