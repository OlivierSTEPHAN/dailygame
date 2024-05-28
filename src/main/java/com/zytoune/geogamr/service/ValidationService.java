package com.zytoune.geogamr.service;

import com.zytoune.geogamr.entity.User;
import com.zytoune.geogamr.entity.Validation;
import com.zytoune.geogamr.repository.ValidationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Transactional
@AllArgsConstructor
@Slf4j
@Service
public class ValidationService {
    private ValidationRepository validationRepository;
    private NotificationService notificationService;

    public void saveOrUpdate(Validation validation){
        this.validationRepository.save(validation);
    }

    public void register(User user){

        this.validationRepository.findByUser(user).ifPresent(validation -> this.validationRepository.delete(validation));

        Random random = new Random();
        int randomInteger = random.nextInt(999999);

        Validation validation = new Validation();
        validation.setUser(user);

        Instant creation = Instant.now();
        Instant expiration = creation.plus(10, ChronoUnit.MINUTES);
        validation.setCreation(creation);
        validation.setExpiration(expiration);

        String code = String.format("%06d", randomInteger);
        validation.setCode(code);

        this.validationRepository.save(validation);
        this.notificationService.sendNotification(validation);
    }


    public Validation getValidationByCode(String code){
        return this.validationRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Votre code de validation est invalide"));
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void deleteExpiredValidation(){
        log.info("Removing expired validations");
        this.validationRepository.deleteAllByExpirationBefore(Instant.now());
    }
}
