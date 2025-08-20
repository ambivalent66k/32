package org.example.storagesystem.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.notification.conditionType.AttributeConditionType;
import org.example.storagesystem.entity.NotificationRule;
import org.example.storagesystem.entity.StorageObject;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.repository.StorageObjectRepository;
import org.example.storagesystem.service.RuleStrategy;
import org.example.storagesystem.utils.AttributeUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AttributeRuleStrategy implements RuleStrategy {
    private final ObjectMapper objectMapper;
    private final StorageObjectRepository storageObjectRepository;

    @Override
    public boolean evaluate(NotificationRule rule) {
        AttributeConditionType attributeType =
                objectMapper.convertValue(rule.getConditionConfig(), AttributeConditionType.class);

        StorageObject storageObject;

        if (attributeType.getObjectId() == null) {
            return false;
        }

        storageObject = storageObjectRepository.findById(attributeType.getObjectId())
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, attributeType.getObjectId()));

        Object fieldValue = AttributeUtils.getFieldValue(storageObject, attributeType.getAttributeName());

        if (fieldValue == null) {
            return false;
        }

        if (fieldValue instanceof LocalDate date) {
            return checkDate(date, attributeType.getNumberValue(), attributeType.getOperator());
        } else if (fieldValue instanceof Integer number) {
            return checkNumber(number, attributeType.getNumberValue(), attributeType.getOperator());
        } else if (fieldValue instanceof String str) {
            return checkString(str, attributeType.getTextValue(), attributeType.getOperator());
        }

        return false;
    }

    private boolean checkDate(LocalDate value, Integer days, String operator) {
        if (days == null) return false;
        LocalDate target = LocalDate.now().minusDays(days);
        return switch (operator) {
            case ">" -> value.isBefore(target);
            case "<" -> value.isAfter(target);
            case "=" -> value.equals(target);
            default -> false;
        };
    }

    private boolean checkNumber(int value, Integer target, String operator) {
        if (target == null) return false;
        return switch (operator) {
            case ">" -> value > target;
            case "<" -> value < target;
            case "=" -> value == target;
            default -> false;
        };
    }

    private boolean checkString(String value, String target, String operator) {
        if (target == null) return false;
        return switch (operator) {
            case "equals" -> value.equals(target);
            case "contains" -> value.contains(target);
            default -> false;
        };
    }
}
