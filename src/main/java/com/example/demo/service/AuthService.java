package com.example.demo.service;


import com.example.demo.SpringSecurity.JwtUtil;
import com.example.demo.model.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    public String login(String user, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user, password)
            );

            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            return jwtUtil.generateToken(userDetails.getUser());
        } catch (AuthenticationException e) {
            throw new RuntimeException("Identifiants invalides");
        }
    }
}
