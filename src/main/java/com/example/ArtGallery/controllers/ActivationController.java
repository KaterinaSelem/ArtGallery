package com.example.ArtGallery.controllers;

import com.example.ArtGallery.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activate")
public class ActivationController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public String activateUser(@RequestParam("code") String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            return "user successfully activated!";
        } else {
            return "Invalid or expired confirmation code!";
        }
    }
}