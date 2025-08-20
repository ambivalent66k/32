package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.storagesystem.entity.NotificationRule;
import org.example.storagesystem.enums.ConditionType;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.service.RuleFactoryService;
import org.example.storagesystem.service.RuleStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RuleFactoryServiceImpl implements RuleFactoryService {
    private final Map<ConditionType, RuleStrategy> strategies;

    @Override
    public boolean evaluate(NotificationRule rule) {
        RuleStrategy strategy = strategies.get(rule.getConditionType());
        if (strategy == null) {
            throw new BusinessException(ErrorCode.STRATEGY_NOT_FOUND);
        }

        return strategy.evaluate(rule);
    }
}
