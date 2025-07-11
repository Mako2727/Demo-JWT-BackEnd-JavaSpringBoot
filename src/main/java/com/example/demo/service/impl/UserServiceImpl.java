package com.example.demo.service.impl;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

public UserDTO getUserDTOById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    return new UserDTO(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null,
        user.getUpdatedAt() != null ? user.getUpdatedAt().format(formatter) : null
    );
}
}
