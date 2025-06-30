package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {

  private Integer id;

  @JsonProperty("rental_id")
  private Long rentalId;

  @JsonProperty("user_id")
  private Long userId;

  @NotBlank(message = "Le message ne peut pas être vide")
  private String message;

  @JsonProperty("created_at")
  private String createdAt;

  @JsonProperty("updated_at")
  private String updatedAt;

  
}
