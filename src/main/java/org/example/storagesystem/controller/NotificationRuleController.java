package org.example.storagesystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.notification.NotificationRuleDto;
import org.example.storagesystem.dto.notification.NotificationRulePatchDto;
import org.example.storagesystem.dto.notification.NotificationRuleRequest;
import org.example.storagesystem.service.NotificationRuleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("http://10.10.146.221/api/v1/notifications/rules")
public class NotificationRuleController {
    private final NotificationRuleService notificationRuleService;

    @PostMapping("")
    public ResponseEntity<NotificationRuleDto> createRule(
            @Valid @RequestBody NotificationRuleRequest notificationRuleRequest,
                                                      BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(notificationRuleService.createRule(notificationRuleRequest), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NotificationRuleDto> updateRule(@Valid @RequestBody NotificationRulePatchDto rulePatchDto,
                                                          @PathVariable(name = "id") Long rule_id,
                                                          BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return new ResponseEntity<>(notificationRuleService.updateRule(rulePatchDto, rule_id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationRuleDto> findRuleById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(notificationRuleService.findRuleById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<NotificationRuleDto>> findAll(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(notificationRuleService.findAll(page, size), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable(name = "id") Long id) {
        notificationRuleService.deleteRuleById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
