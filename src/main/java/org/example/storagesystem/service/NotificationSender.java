package org.example.storagesystem.service;

public interface NotificationSender {
    void send(String recipient, String subject, String text);
}
