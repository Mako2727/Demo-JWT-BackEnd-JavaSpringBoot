package com.example.demo.model;

public class AuthRequest {
    private String name;
    private String password;
    private String email;
    

    // Constructeur vide nécessaire pour la désérialisation JSON
    public AuthRequest() {}

    // Constructeur avec paramètres (optionnel)
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

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
