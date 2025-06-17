package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Préparer une réponse simplifiée (DTO) avec uniquement les champs nécessaires
        var dto = new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt() != null ? user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) : null,
            user.getUpdatedAt() != null ? user.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) : null
        );

        return ResponseEntity.ok(dto);
    }

    // Classe interne DTO simplifiée
    static class UserDTO {
        private Long id;
        private String name;
        private String email;
        private String created_at;
        private String updated_at;

        public UserDTO(Long id, String name, String email, String created_at, String updated_at) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }

        // getters et setters (ou lombok)
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getCreated_at() { return created_at; }
        public void setCreated_at(String created_at) { this.created_at = created_at; }
        public String getUpdated_at() { return updated_at; }
        public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
    }
}
