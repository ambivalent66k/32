package org.example.storagesystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.notification.NotificationRuleDto;
import org.example.storagesystem.dto.notification.NotificationRuleRequest;
import org.example.storagesystem.service.NotificationRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("dev-api.storage-system.ru/v1/notifications/rules")
public class NotificationRuleController {
    private final NotificationRuleService notificationRuleService;

    @PostMapping("")
    public ResponseEntity<NotificationRuleDto> create(
            @Valid @RequestBody NotificationRuleRequest notificationRuleRequest,
                                                      BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(notificationRuleService.createRule(notificationRuleRequest), HttpStatus.CREATED);
    }
}
