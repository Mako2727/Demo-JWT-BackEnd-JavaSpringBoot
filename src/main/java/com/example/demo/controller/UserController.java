package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.service.impl.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

  private final UserServiceImpl userService;


  public UserController(UserServiceImpl userService) {
    this.userService = userService;
  }

  @Operation(summary = "Recuperation d un utilisateur en fonction de son ID")
  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
      UserDTO dto = userService.getUserDTOById(id);
    return ResponseEntity.ok(dto);
  }
}
