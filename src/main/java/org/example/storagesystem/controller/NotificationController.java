package org.example.storagesystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.notification.NotificationDto;
import org.example.storagesystem.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("http://10.10.146.221/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationDto>> findAllNotifications(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(notificationService.findAll(page, size), HttpStatus.OK);
    }
}
