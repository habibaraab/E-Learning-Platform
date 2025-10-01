package com.spring.E_Learning.Service;

import com.spring.E_Learning.DTOs.AuthenticationResponse;
import com.spring.E_Learning.DTOs.LoginRequest;
import com.spring.E_Learning.DTOs.Mapper.UserMapper;
import com.spring.E_Learning.DTOs.UserRequestDto;
import com.spring.E_Learning.Enum.Role;
import com.spring.E_Learning.Enum.TokenType;
import com.spring.E_Learning.Model.Token;
import com.spring.E_Learning.Model.User;
import com.spring.E_Learning.Repository.TokenRepository;
import com.spring.E_Learning.Repository.UserRepository;
import com.spring.E_Learning.Security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserRequestDto request) {
        User user = userMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        Map<String, Object> claims = new HashMap<>();
        String accessToken = jwtService.generateToken(savedUser, claims);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        saveUserToken(savedUser, accessToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Map<String, Object> claims = new HashMap<>();
        String accessToken = jwtService.generateToken(user, claims);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        String userEmail = jwtService.extractUsername(refreshToken);
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (jwtService.isRefreshTokenValid(refreshToken, user)) {
            Map<String, Object> claims = new HashMap<>();
            String newAccessToken = jwtService.generateToken(user, claims);

            revokeAllUserTokens(user);
            saveUserToken(user, newAccessToken);

            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    private void revokeAllUserTokens(User user) {
        var validateUserToken = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validateUserToken.isEmpty()) {
            return;
        }
        validateUserToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validateUserToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
