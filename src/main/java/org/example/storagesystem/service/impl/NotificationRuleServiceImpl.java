package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.storagesystem.dto.notification.NotificationRuleDto;
import org.example.storagesystem.dto.notification.NotificationRulePatchDto;
import org.example.storagesystem.dto.notification.NotificationRuleRequest;
import org.example.storagesystem.entity.NotificationRule;
import org.example.storagesystem.enums.ConditionType;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.mapper.NotificationRuleMapper;
import org.example.storagesystem.repository.NotificationRuleRepository;
import org.example.storagesystem.service.NotificationRuleService;
import org.example.storagesystem.service.NotificationService;
import org.example.storagesystem.service.RuleStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationRuleServiceImpl implements NotificationRuleService {
    private final NotificationService notificationService;
    private final Map<ConditionType, RuleStrategy> strategies;
    private final NotificationRuleMapper notificationRuleMapper;
    private final NotificationRuleRepository notificationRuleRepository;

    @Override
    public NotificationRuleDto createRule(NotificationRuleRequest notificationRuleRequest) {
        NotificationRule notificationRule = notificationRuleMapper.mapTo(notificationRuleRequest);

        notificationRule.setCreatedBy(1L); // временно

        notificationRule = notificationRuleRepository.save(notificationRule);

        return notificationRuleMapper.mapTo(notificationRule);
    }

    @Override
    public NotificationRuleDto updateRule(NotificationRulePatchDto rulePatchDto, Long ruleId) {
        NotificationRule notificationRule = notificationRuleRepository.findById(ruleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTIFICATION_RULE_NOT_FOUND));

        notificationRule = notificationRuleMapper.updateNotificationRule(rulePatchDto, notificationRule);

        notificationRuleRepository.save(notificationRule);

        return notificationRuleMapper.mapTo(notificationRule);
    }

    @Override
    public NotificationRuleDto findRuleById(Long id) {
        NotificationRule notificationRule = notificationRuleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTIFICATION_RULE_NOT_FOUND, id));

        return notificationRuleMapper.mapTo(notificationRule);
    }

    @Override
    public Page<NotificationRuleDto> findAll(int page, int size) {
        Page<NotificationRule> notificationRules = notificationRuleRepository.findAll(PageRequest.of(page, size));

        return notificationRules.map(notificationRuleMapper::mapTo);
    }

    @Override
    public void deleteRuleById(Long id) {
        if (notificationRuleRepository.existsById(id)) {
            notificationRuleRepository.deleteById(id);
        } else {
            throw new BusinessException(ErrorCode.STORAGE_NOT_FOUND, id);
        }
    }

    @Override
    public void checkAndSendNotifications(Long ruleId) {
        NotificationRule notificationRule = notificationRuleRepository.findById(ruleId)
                .orElseThrow();

        RuleStrategy strategy = strategies.get(notificationRule.getConditionType());

        if (strategy == null) {
            throw new BusinessException(ErrorCode.STRATEGY_NOT_FOUND);
        }

        boolean triggered;

        try {
            triggered = strategy.evaluate(notificationRule);
        } catch (Exception ex) {
            log.error("Error evaluating rule {}: {}", notificationRule.getId(), ex.getMessage(), ex);
            return;
        }

        if (triggered && !notificationRule.getRecipientsConfig().isEmpty()) {
            notificationService.createNotificationForRule(notificationRule);
        }
    }
}
