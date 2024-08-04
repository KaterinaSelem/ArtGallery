package com.example.ArtGallery.service;

import com.example.ArtGallery.domain.entity.ConfirmationCode;
import com.example.ArtGallery.domain.entity.User;
import com.example.ArtGallery.repositories.ConfirmationCodeRepository;
import com.example.ArtGallery.service.interfaces.ConfirmationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationCodeRepository repository;

    public ConfirmationServiceImpl(ConfirmationCodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public String generateConfirmationCode(User user) {
        String code = UUID.randomUUID().toString();
        LocalDateTime expired = LocalDateTime.now().plusMinutes(60);
        ConfirmationCode confirmationCode = new ConfirmationCode(code, expired, user);
        repository.save(confirmationCode);
        return code;
    }
}