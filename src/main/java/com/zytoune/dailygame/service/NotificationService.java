package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.auth.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {

    JavaMailSender javaMailSender;

    public void sendNotification(Validation validation){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("no-reply@moi.bzh");
        mailMessage.setTo(validation.getUser().getEmail());
        mailMessage.setSubject("Votre code d'activation");

        String mailContent = String.format("Bonjour %s, <br/> Votre code d'activation est %s",
                validation.getUser().getUsername(),
                validation.getCode()
        );
        mailMessage.setText(mailContent);

        this.javaMailSender.send(mailMessage);
    }
}
