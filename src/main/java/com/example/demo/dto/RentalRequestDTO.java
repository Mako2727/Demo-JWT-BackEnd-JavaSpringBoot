package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RentalRequestDTO {

  @NotBlank(message = "Le nom est requis")
  private String name;

  @NotNull(message = "La surface est requise")
  @Min(value = 1, message = "La surface doit être supérieure à 0")
  private int   surface;

 @NotNull(message = "Le prix est requis")
  @Min(value = 0, message = "Le prix doit être positif ou nul")
  private double  price;

  @NotBlank(message = "La description est requise")
  private String description;

  private MultipartFile picture ;

  

}
