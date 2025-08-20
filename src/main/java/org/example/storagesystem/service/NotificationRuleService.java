package org.example.storagesystem.service;

import org.example.storagesystem.dto.notification.NotificationRuleDto;
import org.example.storagesystem.dto.notification.NotificationRulePatchDto;
import org.example.storagesystem.dto.notification.NotificationRuleRequest;
import org.springframework.data.domain.Page;

public interface NotificationRuleService {
    NotificationRuleDto createRule(NotificationRuleRequest notificationRuleRequest);

    NotificationRuleDto updateRule(NotificationRulePatchDto rulePatchDto, Long ruleId);

    NotificationRuleDto findRuleById(Long id);

    Page<NotificationRuleDto> findAll(int page, int size);

    void deleteRuleById(Long id);

    void checkAndSendNotifications(Long ruleId);
}
