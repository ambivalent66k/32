package org.example.storagesystem.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.notification.conditionType.CapacityConditionType;
import org.example.storagesystem.entity.Cell;
import org.example.storagesystem.entity.NotificationRule;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.repository.CellRepository;
import org.example.storagesystem.service.RuleStrategy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CapacityRuleStrategy implements RuleStrategy {
    private final CellRepository cellRepository;
    private final ObjectMapper objectMapper;

    @Override
    public boolean evaluate(NotificationRule rule) {
        CapacityConditionType conditionType = objectMapper.convertValue(
                rule.getConditionConfig(),
                CapacityConditionType.class
        );

        Cell cell = cellRepository.findWithObjectsById(conditionType.getCellId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, conditionType.getCellId()));

        int occupiedQuantity = occupiedQuantity(cell);
        int maxCapacity = cell.getCapacity();


        int valueToCheck = conditionType.getThresholdType().equalsIgnoreCase("percent") ?
                (occupiedQuantity * 100 / Math.max(1, maxCapacity)) :
                occupiedQuantity;

        return compare(valueToCheck, conditionType.getThresholdValue(), conditionType.getOperator());
    }

    @Override
    public boolean compare(int current, int threshold, String operator) {
        return RuleStrategy.super.compare(current, threshold, operator);
    }

    private int occupiedQuantity(Cell cell) {
        return cell.getStorageObjects().stream()
                .mapToInt(obj -> obj.getQuantity() * obj.getVolumePerUnit())
                .sum();
    }
}
