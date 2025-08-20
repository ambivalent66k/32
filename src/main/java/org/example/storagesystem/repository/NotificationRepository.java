package org.example.storagesystem.repository;

import org.example.storagesystem.entity.Notification;
import org.example.storagesystem.entity.Storage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @EntityGraph(attributePaths = {"rule"})
    @Query("select s from Notification s")
    Page<Notification> findAllWithRules(Pageable pageable);
}
