package com.example.ArtGallery;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {

    public static void main(String[] args) {
        // Получить зашифрованный вариант пароля.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "111";
        String encodedPassword = encoder.encode(password);
        System.out.println(encodedPassword);
    }
}
