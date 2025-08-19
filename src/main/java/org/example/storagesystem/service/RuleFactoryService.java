package org.example.storagesystem.service;

import org.example.storagesystem.entity.NotificationRule;

public interface RuleFactoryService {
    boolean evaluate(NotificationRule rule);
}
