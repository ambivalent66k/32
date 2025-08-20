package org.example.storagesystem.config;

import org.example.storagesystem.enums.ConditionType;
import org.example.storagesystem.service.RuleStrategy;
import org.example.storagesystem.service.impl.AttributeRuleStrategy;
import org.example.storagesystem.service.impl.CapacityRuleStrategy;
import org.example.storagesystem.service.impl.CountRuleStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class StrategyConfig {
    @Bean
    public Map<ConditionType, RuleStrategy> strategies(
            CapacityRuleStrategy capacity,
            CountRuleStrategy count,
            AttributeRuleStrategy attribute
    ) {
        return Map.of(
                ConditionType.CAPACITY, capacity,
                ConditionType.COUNT, count,
                ConditionType.ATTRIBUTE, attribute
        );
    }
}
