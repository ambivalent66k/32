package org.example.storagesystem.repository;

import org.example.storagesystem.entity.NotificationRule;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRuleRepository extends JpaRepository<NotificationRule, Long> {
    List<NotificationRule> findAllByIsActiveTrue();
}
