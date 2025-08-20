package org.example.storagesystem.service;

import org.example.storagesystem.dto.notification.NotificationDto;
import org.example.storagesystem.entity.NotificationRule;
import org.springframework.data.domain.Page;

public interface NotificationService {
    void createNotificationForRule(NotificationRule rule);

    Page<NotificationDto> findAll(int page, int size);
}
