package com.spring.E_Learning.DTOs;


import com.spring.E_Learning.Enum.Role;
import lombok.Data;

@Data
public class ProfileResponseDto {
    private int id;
    private String name;
    private String email;
    private Role role;
    private String password;
    private boolean activa;
}
