package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalDetailDTO {

  private Long id;

  private String name;

  private int surface;

  private double price;

  private String description;

  @JsonProperty("created_at")
  private String created_at;

  @JsonProperty("updated_at")
  private String updated_at;

  @JsonProperty("owner_id")
  private Long ownerId;

  @JsonProperty("picture")
  private String picture;

 
}
