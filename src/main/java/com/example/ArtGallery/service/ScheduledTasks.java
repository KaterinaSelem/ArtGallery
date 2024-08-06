package com.example.ArtGallery.service;

import jakarta.persistence.Column;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final ConfirmationServiceImpl confirmationService;

    public ScheduledTasks(ConfirmationServiceImpl confirmationService) {
        this.confirmationService = confirmationService;
    }

    // Планировщик, запускающийся каждый день в полночь
    @Scheduled(cron = "0 */2 * * * ?")
    public void cleanUpExpiredConfirmationCodes() {
        confirmationService.removeExpiredConfirmationCodesAndUsers();
    }
}