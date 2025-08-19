package org.example.storagesystem.mapper;

import org.example.storagesystem.entity.Notification;
import org.example.storagesystem.entity.NotificationRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "message", source = "messageText")
    @Mapping(target = "rule", ignore = true)
    @Mapping(target = "id", ignore = true)
    Notification mapFrom(NotificationRule notificationRule);
}
