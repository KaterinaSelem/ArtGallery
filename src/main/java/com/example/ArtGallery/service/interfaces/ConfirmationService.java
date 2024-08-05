package com.example.ArtGallery.service.interfaces;

import com.example.ArtGallery.domain.entity.ConfirmationCode;
import com.example.ArtGallery.domain.entity.User;

public interface ConfirmationService {

    String generateConfirmationCode(User user);

    ConfirmationCode getConfirmationCode(String code);

    void deleteConfirmationCode(ConfirmationCode confirmationCode);
}
