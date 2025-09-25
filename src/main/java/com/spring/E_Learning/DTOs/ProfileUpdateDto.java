package com.spring.E_Learning.DTOs;

import lombok.Data;

@Data
public class ProfileUpdateDto {
    private String name;
    private String oldPassword;
    private String newPassword;
}
