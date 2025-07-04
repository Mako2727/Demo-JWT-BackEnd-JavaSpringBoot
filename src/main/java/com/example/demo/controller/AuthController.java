package com.example.demo.controller;

import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.UserMeDTO;
import com.example.demo.model.AuthRequest;
import com.example.demo.service.impl.AuthServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthServiceImpl authService;

  public AuthController(AuthServiceImpl authService) {
    this.authService = authService;
  }

  @Operation(summary = "Recuperation du token")
  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest) {
    String token = authService.login(authRequest.getEmail(), authRequest.getPassword());
    return ResponseEntity.ok(Map.of("token", token));
  }

   @Operation(summary = "Creation d un utilisateur")
  @PostMapping("/register")
  public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterDTO request) {
    String token = authService.register(request);
    return ResponseEntity.ok(Map.of("token", token));
  }

  @Operation(summary = "Recuperation des informations de l utilisateur")
  @GetMapping("/me")
  public ResponseEntity<UserMeDTO> getCurrentUser(Authentication authentication) {
    UserMeDTO dto = authService.getCurrentUser(authentication);
    return ResponseEntity.ok(dto);
}
}
