package com.example.ArtGallery.repositories;


import com.example.ArtGallery.domain.entity.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

    Optional<ConfirmationCode> findByCode(String code);

    List<ConfirmationCode> findAllByExpiredBefore(LocalDateTime now);
}
