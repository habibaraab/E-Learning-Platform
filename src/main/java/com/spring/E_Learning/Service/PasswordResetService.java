package com.spring.E_Learning.Service;


import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;


    public void sendResetCode(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        String code = String.valueOf((int)(Math.random() * 900000) + 100000);
        user.setResetCode(code);
        user.setResetCodeExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
        mailService.sendResetCode(user.getEmail(), code);
    }

    public void resetPassword(String email, String code, String newPassword) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getResetCode() == null || user.getResetCodeExpiry() == null) {
            throw new IllegalArgumentException("No reset request found");
        }

        if (LocalDateTime.now().isAfter(user.getResetCodeExpiry())) {
            throw new IllegalArgumentException("Reset code expired");
        }

        if (!user.getResetCode().equals(code)) {
            throw new IllegalArgumentException("Invalid reset code");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetCode(null);
        user.setResetCodeExpiry(null);
        userRepository.save(user);
    }


}
