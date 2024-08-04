package com.example.ArtGallery.service.interfaces;

import com.example.ArtGallery.domain.entity.User;

public interface ConfirmationService {

    String generateConfirmationCode(User user);
}
