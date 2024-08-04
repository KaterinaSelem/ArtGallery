package com.example.ArtGallery.controllers;

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
    public Response register(@RequestBody User user) {
        service.register(user);
        return new Response("Registration complete. You have been sent an email with a confirmation link. " +
                "The link will be valid for 1 hour." +
                "Please check your email. ");
    }
}
