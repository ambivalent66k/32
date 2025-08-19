package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.storagesystem.service.NotificationSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String defaultFrom;

    @Override
    public void send(String recipient, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(defaultFrom);
            message.setTo(recipient);
            message.setSubject(subject);
            message.setText(text);
            System.out.println("mailSender.send(message);");
        } catch (Exception e) {
            log.error("Error for send {}: {}", recipient, e.getMessage(), e);
        }

    }
}
