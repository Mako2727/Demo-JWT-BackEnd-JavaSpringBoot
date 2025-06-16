package com.example.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.demo.model.User;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // ou retourne les rôles si tu en as
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

      // Ajoute si besoin
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getUsername() {
        return user.getName();  // ou getEmail() selon ce que tu utilises
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
