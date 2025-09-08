package com.spring.E_Learning.DTOs;

import com.spring.E_Learning.Enum.Role;
import lombok.Data;

@Data
public class UserResponseDto {
    private int id;
    private String name;
    private String email;
    private Role role;
}
