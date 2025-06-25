package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RentalDetailDTO {

  private Long id;

  private String name;

  private int surface;

  private double price;

  private String description;

  private OwnerDTO owner;

  @JsonProperty("created_at")
  private String created_at;

  @JsonProperty("updated_at")
  private String updatedAt;

  @JsonProperty("owner_id")
  private Long ownerId;

  @JsonProperty("picture")
  private String picture;

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

  public OwnerDTO getOwner() {
    return owner;
  }

  public void setOwner(OwnerDTO owner) {
    this.owner = owner;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public String getUpdate_at() {
    return updatedAt;
  }

  public void setUpdated_at(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }
}
