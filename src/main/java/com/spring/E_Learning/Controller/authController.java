package com.spring.E_Learning.Controller;


import com.spring.E_Learning.DTOs.*;
import com.spring.E_Learning.Service.AuthService;
import com.spring.E_Learning.Service.PasswordResetService;
import com.spring.E_Learning.Service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class authController {


    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final ProfileService profileService;



    @PostMapping("/Login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest , @RequestParam Map<String,Object> claims) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/Register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRequestDto registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/Forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.sendResetCode(request.getEmail());
        return ResponseEntity.ok("Reset code sent to your email");
    }

    @PostMapping("/Reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(
                request.getEmail(),
                request.getCode(),
                request.getNewPassword()
        );
        return ResponseEntity.ok("Password has been reset successfully");
    }


    @GetMapping("/myProfile")
    public ResponseEntity<ProfileResponseDto> getMyProfile() {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    @PutMapping("/UpdateProfile")
    public ResponseEntity<ProfileResponseDto> updateProfile(
            @RequestBody @Valid ProfileUpdateDto dto) {
        return ResponseEntity.ok(profileService.updateProfile(dto));
    }

}