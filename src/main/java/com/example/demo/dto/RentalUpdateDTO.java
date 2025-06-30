package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalUpdateDTO {
  private Long id;

  @NotBlank(message = "Le nom ne peut pas être vide")
  private String name;

  @Min(value = 1, message = "La surface doit être un entier positif")
  private int surface;

  @NotNull(message = "Le prix est obligatoire")
  @Positive(message = "Le prix doit être un nombre positif")
  private double price;

  @NotBlank(message = "La description ne peut pas être vide")
  private String description;

  @JsonProperty("owner_id")
  private Long ownerId;

  
}
