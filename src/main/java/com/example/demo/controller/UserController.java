package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import com.example.demo.dto.UserDTO;

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

    
}
