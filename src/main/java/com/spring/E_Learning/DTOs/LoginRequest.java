package com.spring.E_Learning.DTOs;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class LoginRequest {
    private String email;
    private String password;
}
