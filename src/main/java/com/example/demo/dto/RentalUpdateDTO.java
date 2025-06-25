package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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

  // Getters et Setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSurface() {
    return surface;
  }

  public void setSurface(int surface) {
    this.surface = surface;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }
}
