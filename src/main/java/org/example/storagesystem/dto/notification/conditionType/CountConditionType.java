package org.example.storagesystem.dto.notification.conditionType;

import lombok.Data;

@Data
public class CountConditionType {
    private String objectName;
    private int thresholdValue;
    private String operator;
}
