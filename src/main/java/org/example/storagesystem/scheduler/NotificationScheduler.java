package org.example.storagesystem.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.storagesystem.entity.NotificationRule;
import org.example.storagesystem.repository.NotificationRuleRepository;
import org.example.storagesystem.service.NotificationRuleService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class NotificationScheduler {
    private final NotificationRuleRepository notificationRuleRepository;
    private final NotificationRuleService notificationRuleService;
    private final ThreadPoolTaskExecutor notificationSchedulerExecutor;

    @Scheduled(fixedDelay = 6000)
    public void schedule() {
        List<Long> rulesIds = notificationRuleRepository.findAllByIsActiveTrue().stream()
                .map(NotificationRule::getId).toList();

        for (Long rule : rulesIds) {
            try {
                notificationSchedulerExecutor.execute(() -> {
                    try {
                        notificationRuleService.checkAndSendNotifications(rule);
                    } catch (Exception ex) {
                        log.error("Error processing rule {}: {}", rule, ex.getMessage(), ex);
                    }
                });
            } catch (RejectedExecutionException rex) {
                log.warn("Scheduler executor rejected task for rule {}: {}", rule, rex.getMessage());
            }
        }
    }
}
