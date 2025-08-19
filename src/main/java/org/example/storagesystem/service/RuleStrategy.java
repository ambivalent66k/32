package org.example.storagesystem.service;

import org.example.storagesystem.entity.NotificationRule;

public interface RuleStrategy {
    boolean evaluate(NotificationRule rule);

    default boolean compare(int current, int threshold, String operator) {
        return switch (operator) {
            case ">=" -> current >= threshold;
            case "<=" -> current <= threshold;
            case ">" -> current > threshold;
            case "<" -> current < threshold;
            case "==" -> current == threshold;
            default -> false;
        };
    }
}
