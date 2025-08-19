package org.example.storagesystem.dto.notification;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.storagesystem.enums.ConditionType;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRuleRequest {
    @NotBlank(message = "it cannot be empty")
    @Size(max = 50, message = "wrong size")
    private String title;

    private ConditionType conditionType;

    private Map<String, Object> conditionConfig;

    private Map<String, Object> recipientsConfig;

    @NotBlank(message = "it cannot be empty")
    @Size(max = 500, message = "wrong size")
    private String messageText;

    @NotNull
    private Long createdBy;

    private boolean isActive;
}
