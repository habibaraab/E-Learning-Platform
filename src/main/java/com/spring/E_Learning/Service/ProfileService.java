package com.spring.E_Learning.Service;


import com.spring.E_Learning.DTOs.Mapper.UserMapper;
import com.spring.E_Learning.DTOs.ProfileResponseDto;
import com.spring.E_Learning.DTOs.ProfileUpdateDto;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Logged-in user not found"));
    }


    public ProfileResponseDto getMyProfile() {
        User user = getLoggedInUser();
        return userMapper.toProfileResponseDto(user);
    }

    public ProfileResponseDto updateProfile(ProfileUpdateDto dto) {
        User user = getLoggedInUser();

        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }

        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            if (dto.getOldPassword() == null ||
                    !passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Old password is incorrect");
            }
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        userRepository.save(user);
        return userMapper.toProfileResponseDto(user);
    }



}
