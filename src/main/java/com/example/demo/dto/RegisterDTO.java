package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

  @NotBlank(message = "Le nom est obligatoire")
  private String name;

  @Email(message = "L'adresse e-mail est invalide")
  @NotBlank(message = "L'email est obligatoire")
  private String email;

  @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
  private String password;

  // Getters et Setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
