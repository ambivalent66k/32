package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.notification.NotificationDto;
import org.example.storagesystem.entity.Notification;
import org.example.storagesystem.entity.NotificationRule;
import org.example.storagesystem.mapper.NotificationMapper;
import org.example.storagesystem.repository.NotificationRepository;
import org.example.storagesystem.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationMapper notificationMapper;
    private final NotificationDispatcher notificationDispatcher;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void createNotificationForRule(NotificationRule rule) {
        Notification notification = notificationMapper.mapFrom(rule);

        notification.setRule(rule);

        notificationRepository.save(notification);

        notificationDispatcher.dispatch(notification, rule);
    }

    @Override
    public Page<NotificationDto> findAll(int page, int size) {
        Page<Notification> notifications = notificationRepository.findAllWithRules(PageRequest.of(page, size));
        return notifications.map(notificationMapper::toDto);
    }
}
