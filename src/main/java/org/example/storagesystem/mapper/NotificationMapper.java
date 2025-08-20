package org.example.storagesystem.mapper;

import org.example.storagesystem.dto.notification.NotificationDto;
import org.example.storagesystem.entity.Notification;
import org.example.storagesystem.entity.NotificationRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "message", source = "messageText")
    @Mapping(target = "rule", ignore = true)
    @Mapping(target = "id", ignore = true)
    Notification mapFrom(NotificationRule notificationRule);

    @Mapping(target = "ruleId", source = "rule.id")
    NotificationDto toDto(Notification notification);
}
