package com.example.demo.service;

import org.springframework.security.core.Authentication;

import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.UserMeDTO;
import com.example.demo.model.User;


public interface AuthService {
  public String login(String email, String password) ;

  public String register(RegisterDTO request) ;

  public UserMeDTO  getCurrentUser(Authentication authentication) ;

  public UserMeDTO mapToUserMeDTO(User user) ;
}
