package org.example.storagesystem.service;

import org.example.storagesystem.dto.notification.NotificationRuleDto;
import org.example.storagesystem.dto.notification.NotificationRuleRequest;

public interface NotificationRuleService {
    NotificationRuleDto createRule(NotificationRuleRequest notificationRuleRequest);

    void checkAndSendNotifications(Long ruleId);
}
