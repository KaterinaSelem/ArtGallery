package com.example.ArtGallery.security.sec_service;


import com.example.ArtGallery.domain.entity.User;
import com.example.ArtGallery.security.sec_dto.TokenResponseDto;
import com.example.ArtGallery.service.UserServiceImpl;
import com.example.ArtGallery.service.interfaces.UserService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private UserService userService;
    private TokenService tokenService;
    private Map<String, String> refreshStorage;
    private BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserService userService, TokenService tokenService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.refreshStorage = new HashMap<>();
    }

    public TokenResponseDto login(User inboundUser) {
        String username = inboundUser.getUsername();
        User foundUser;

        try {
            foundUser = (User) userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // Возвращаем null или выбрасываем исключение, если пользователь не найден
            return new TokenResponseDto(null, null);
        }

        if (foundUser == null || !passwordEncoder.matches(inboundUser.getPassword(), foundUser.getPassword())) {
            // Возвращаем null или выбрасываем исключение, если пароль неверный
            return new TokenResponseDto(null, null);
        }

        String accessToken = tokenService.generateAccessToken(foundUser);
        String refreshToken = tokenService.generateRefreshToken(foundUser);
        refreshStorage.put(username, refreshToken);
        return new TokenResponseDto(accessToken, refreshToken);
    }

    public TokenResponseDto getNewAccessToken(String inboundRefreshToken) {
        Claims refreshClaims = tokenService.getRefreshClaims(inboundRefreshToken);
        String username = refreshClaims.getSubject();
        String savedRefreshToken = refreshStorage.get(username);

        if (savedRefreshToken != null && savedRefreshToken.equals(inboundRefreshToken)) {
            User user = (User) userService.loadUserByUsername(username);
            String accessToken = tokenService.generateAccessToken(user);
            return new TokenResponseDto(accessToken, null);
        } else {
            return new TokenResponseDto(null, null);
        }
    }
}