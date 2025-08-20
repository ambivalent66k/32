package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.storagesystem.entity.Notification;
import org.example.storagesystem.entity.NotificationRule;
import org.example.storagesystem.enums.notification.Status;
import org.example.storagesystem.repository.NotificationRepository;
import org.example.storagesystem.repository.NotificationRuleRepository;
import org.example.storagesystem.service.NotificationSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProcessor {
    private final NotificationRepository notificationRepository;
    private final NotificationRuleRepository notificationRuleRepository;
    private final List<NotificationSender> senders;

    @Transactional
    public void process(Notification notification, NotificationRule rule) {
        List<String> recipients = extractEmails(rule.getRecipientsConfig());
        if (recipients.isEmpty()) return;

        boolean allSuccess = true;

        for (String recipient : recipients) {
            try {
                for (NotificationSender sender : senders) {
                    sender.send(recipient, rule.getTitle(), rule.getMessageText());
                }
            } catch (Exception e) {
                allSuccess = false;
                log.error("Ошибка при отправке {}: {}", recipient, e.getMessage(), e);
            }
        }

        if (allSuccess) {
            notification.setStatus(Status.SENT);
            rule.setIsActive(false);
        } else {
            notification.setStatus(Status.FAILED);
        }

        notificationRepository.save(notification);
        notificationRuleRepository.save(rule);
    }

    private List<String> extractEmails(Map<String, Object> config) {
        List<String> emails = new ArrayList<>();
        Object raw = config.get("email_users");
        if (raw instanceof List<?>) {
            for (Object val : (List<?>) raw) {
                emails.add(String.valueOf(val));
            }
        }
        return emails;
    }
}
