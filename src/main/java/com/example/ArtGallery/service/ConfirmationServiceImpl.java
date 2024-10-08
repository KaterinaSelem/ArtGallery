package com.example.ArtGallery.service;

import com.example.ArtGallery.domain.entity.ConfirmationCode;
import com.example.ArtGallery.domain.entity.User;
import com.example.ArtGallery.repositories.ConfirmationCodeRepository;
import com.example.ArtGallery.repositories.UserRepository;
import com.example.ArtGallery.service.interfaces.ConfirmationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationCodeRepository repository;
    private final UserRepository userRepository;

    public ConfirmationServiceImpl(ConfirmationCodeRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public String generateConfirmationCode(User user) {
        String code = UUID.randomUUID().toString();
        LocalDateTime expired = LocalDateTime.now().plusMinutes(5);
        ConfirmationCode confirmationCode = new ConfirmationCode(code, expired, user);
        repository.save(confirmationCode);
        return code;
    }

    @Override
    public ConfirmationCode getConfirmationCode(String code) {
        return repository.findByCode(code).orElse(null);
    }

    @Override
    public void deleteConfirmationCode(ConfirmationCode confirmationCode) {
        repository.delete(confirmationCode);
    }

    @Transactional
    public void removeExpiredConfirmationCodesAndUsers() {
        List<ConfirmationCode> expiredCodes = repository.findAllByExpiredBefore(LocalDateTime.now());
        for (ConfirmationCode confirmationCode : expiredCodes) {
            User user = confirmationCode.getUser();
            repository.delete(confirmationCode);
            if (user != null) {
                userRepository.delete(user);
            }
        }
    }
}