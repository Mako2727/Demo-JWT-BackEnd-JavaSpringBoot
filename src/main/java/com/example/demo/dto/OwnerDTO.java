package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerDTO {
  private Long id;
  private String name;
  private String email;
  private String created_at;
  private String updated_at;

  // Constructeur complet
  public OwnerDTO(Long id, String name, String email, String created_at, String updated_at) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.created_at = created_at;
    this.updated_at = updated_at;
  }

  // Constructeur par défaut
  public OwnerDTO() {}

}
