package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
  private String name;
  private String password;
  private String email;

  
  public AuthRequest() {}


  public AuthRequest(String name, String password, String email) {
    this.name = name;
    this.password = password;
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
