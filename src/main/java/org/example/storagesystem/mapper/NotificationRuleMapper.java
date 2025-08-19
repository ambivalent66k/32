package org.example.storagesystem.mapper;

import org.example.storagesystem.dto.notification.NotificationRuleDto;
import org.example.storagesystem.dto.notification.NotificationRuleRequest;
import org.example.storagesystem.entity.NotificationRule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationRuleMapper {
    NotificationRule mapTo(NotificationRuleRequest notificationRuleRequest);

    NotificationRuleDto mapTo(NotificationRule notificationRule);
}
