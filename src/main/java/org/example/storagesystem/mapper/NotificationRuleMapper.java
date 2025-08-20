package org.example.storagesystem.mapper;

import org.example.storagesystem.dto.notification.NotificationRuleDto;
import org.example.storagesystem.dto.notification.NotificationRulePatchDto;
import org.example.storagesystem.dto.notification.NotificationRuleRequest;
import org.example.storagesystem.entity.NotificationRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationRuleMapper {
    @Mapping(target = "isActive", source = "active")
    NotificationRule mapTo(NotificationRuleRequest notificationRuleRequest);

    NotificationRuleDto mapTo(NotificationRule notificationRule);

    NotificationRule updateNotificationRule(NotificationRulePatchDto rulePatchDto,
                                            @MappingTarget NotificationRule notificationRule);
}
