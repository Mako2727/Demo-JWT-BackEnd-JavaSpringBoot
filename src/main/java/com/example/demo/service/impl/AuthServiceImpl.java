package com.example.demo.service.impl;

import com.example.demo.SpringSecurity.JwtUtil;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.UserMeDTO;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtUtil jwtUtil;

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  public String login(String email, String password) {
   
      Authentication auth =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(email, password));
      CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
      return jwtUtil.generateToken(userDetails.getUser());
   
  }

  public String register(RegisterDTO request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email déjà utilisé");
    }

   
      User user = new User();
      user.setEmail(request.getEmail());
      user.setName(request.getName());
      user.setPassword(passwordEncoder.encode(request.getPassword()));
      userRepository.save(user);

      return jwtUtil.generateToken(user);
    
  }

  public UserMeDTO  getCurrentUser(Authentication authentication) {
 CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();


    User user = userRepository
        .findByEmail(customUserDetails.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

    return mapToUserMeDTO(user);
  }

  public UserMeDTO mapToUserMeDTO(User user) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    UserMeDTO dto = new UserMeDTO();
    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setName(user.getName());
    dto.setCreated_at(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null);
    dto.setUpdated_at(user.getUpdatedAt() != null ? user.getUpdatedAt().format(formatter) : "");
    return dto;
}
}
