package com.example.ArtGallery.controllers;

import com.example.ArtGallery.domain.DTO.RegisterDTO;
import com.example.ArtGallery.domain.entity.User;
import com.example.ArtGallery.exception_handling.Response;
import com.example.ArtGallery.service.interfaces.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService service;

    public RegistrationController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public Response register(@RequestBody RegisterDTO registerDTO) {
        try {
            service.register(registerDTO);
            return new Response("Registration complete. You have been sent an email with a confirmation link. " +
                    "The link will be valid for 1 hour. Please check your email.");
        } catch (RuntimeException e) {
            return new Response("Registration failed: " + e.getMessage());
        }
    }

    private static class Response {
        private final String message;

        public Response(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
