package org.example.storagesystem.dto.notification.conditionType;

import lombok.Data;

@Data
public class AttributeConditionType {
    private Long objectId;
    private String attributeName;
    private String operator;
    private String textValue;
    private Integer numberValue;
}
