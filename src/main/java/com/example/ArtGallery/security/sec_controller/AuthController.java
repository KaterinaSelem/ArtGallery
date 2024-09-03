package com.example.ArtGallery.security.sec_controller;


import com.example.ArtGallery.domain.entity.User;
import com.example.ArtGallery.security.sec_dto.RefreshRequestDto;
import com.example.ArtGallery.security.sec_dto.TokenResponseDto;
import com.example.ArtGallery.security.sec_service.AuthService;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody User user) {
        try {
            TokenResponseDto tokenResponse = service.login(user);
            if (tokenResponse.getAccessToken() == null) {
                // Неверный логин или пароль
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(tokenResponse);
            }
            // Успешный вход
            return ResponseEntity.ok(tokenResponse);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            // Ошибка аутентификации
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponseDto(null, null));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> getNewAccessToken(@RequestBody RefreshRequestDto request) {
        TokenResponseDto tokenResponse = service.getNewAccessToken(request.getRefreshToken());
        if (tokenResponse.getAccessToken() == null) {
            // Неудачная попытка обновления токена
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tokenResponse);
        }
        // Успешное обновление токена
        return ResponseEntity.ok(tokenResponse);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        // Логируем ошибку и возвращаем сообщение
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
}