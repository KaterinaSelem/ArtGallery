package com.example.ArtGallery.service.interfaces;


import com.example.ArtGallery.domain.entity.User;

public interface EmailService {

    void sendConfirmationEmail(User user);
}
