package org.example.storagesystem.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.notification.conditionType.CountConditionType;
import org.example.storagesystem.entity.NotificationRule;
import org.example.storagesystem.repository.StorageObjectRepository;
import org.example.storagesystem.service.RuleStrategy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CountRuleStrategy implements RuleStrategy {
    private final ObjectMapper objectMapper;
    private final StorageObjectRepository storageObjectRepository;

    @Override
    public boolean evaluate(NotificationRule rule) {
        CountConditionType conditionType = objectMapper.convertValue(
                rule.getConditionConfig(),
                CountConditionType.class
        );

        int count = storageObjectRepository.countByName(conditionType.getObjectName());

        return compare(count, conditionType.getThresholdValue(), conditionType.getOperator());
    }

    @Override
    public boolean compare(int current, int threshold, String operator) {
        return RuleStrategy.super.compare(current, threshold, operator);
    }
}
