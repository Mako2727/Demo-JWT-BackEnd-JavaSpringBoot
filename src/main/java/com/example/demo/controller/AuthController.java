package com.example.demo.controller;

import com.example.demo.dto.RegisterDTO;
import com.example.demo.model.AuthRequest;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest) {      
            String token = authService.login(authRequest.getEmail(), authRequest.getPassword());
            return ResponseEntity.ok(Map.of("token", token));      
    }

    @PostMapping("/register")
  public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterDTO request) {   
        String token = authService.register(request);
        return ResponseEntity.ok(Map.of("token", token));   
}

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {        
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = authService.getCurrentUser(customUserDetails.getEmail());

            return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "name", user.getName(),
                "created_at", user.getCreatedAt(),
                "updated_at", user.getUpdatedAt() != null ? user.getUpdatedAt() : ""
            ));
       
    }
}

