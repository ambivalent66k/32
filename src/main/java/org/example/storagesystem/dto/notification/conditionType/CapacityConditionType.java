package org.example.storagesystem.dto.notification.conditionType;

import lombok.Data;

@Data
public class CapacityConditionType {
    private Long cellId;
    private String thresholdType;
    private int thresholdValue;
    private String operator;
}
