package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RentalRequestDTO {

    @NotBlank(message = "Le nom est requis")
    private String name;

    @NotBlank(message = "La surface est requise")
    @Pattern(regexp = "\\d+", message = "La surface doit être un entier")
    private String surface;

    @NotBlank(message = "Le prix est requis")
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Le prix doit être un nombre valide")
    private String price;

    @NotBlank(message = "La description est requise")
    private String description;

    // Getters et setters obligatoires pour @ModelAttribute

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
