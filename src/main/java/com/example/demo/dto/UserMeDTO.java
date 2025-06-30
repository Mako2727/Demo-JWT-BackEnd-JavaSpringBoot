package com.example.demo.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMeDTO {
    private Long id;
    private String email;
    private String name;
    private String created_at;
    private String updated_at;
}
