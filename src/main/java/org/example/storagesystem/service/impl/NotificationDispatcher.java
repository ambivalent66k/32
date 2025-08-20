package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.storagesystem.entity.Notification;
import org.example.storagesystem.entity.NotificationRule;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationDispatcher {
    private final NotificationProcessor notificationProcessor;
    private final ThreadPoolTaskExecutor notificationDispatcherExecutor;

    public void dispatch(Notification notification, NotificationRule rule) {
        notificationDispatcherExecutor.execute(() -> {
            try {
                notificationProcessor.process(notification, rule);
            } catch (Exception e) {
                log.error("Ошибка при обработке уведомления {}: {}", notification.getId(), e.getMessage(), e);
            }
        });
    }
}
