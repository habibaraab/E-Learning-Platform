package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.AuthenticationResponse;
import com.spring.E_Learning.DTOs.LoginRequest;
import com.spring.E_Learning.DTOs.UserRequestDto;
import com.spring.E_Learning.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/auth")
public class authController {


    @Autowired
    private  AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest , @RequestParam Map<String,Object> claims) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRequestDto registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }
}