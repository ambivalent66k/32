package org.example.storagesystem.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRulePatchDto {
    @Size(min = 1, max = 50, message = "wrong size")
    private String title;

    private Map<String, Object> conditionConfig;

    private Map<String, Object> recipientsConfig;

    @Size(min = 1, max = 500, message = "wrong size")
    private String messageText;

    @JsonProperty("is_active")
    private Boolean isActive;
}
