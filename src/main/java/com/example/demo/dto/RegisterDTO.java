package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {

  @NotBlank(message = "Le nom est obligatoire")
  private String name;

  @Email(message = "L'adresse e-mail est invalide")
  @NotBlank(message = "L'email est obligatoire")
  private String email;

  @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
  private String password;

}
