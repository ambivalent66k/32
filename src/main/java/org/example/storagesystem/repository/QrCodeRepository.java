package org.example.storagesystem.repository;

import org.example.storagesystem.entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QRCode, Long> {
    boolean existsByStorageId(Long storageId);
    Optional<QRCode> findByStorageId(Long storageId);

    boolean existsByCellId(Long cellId);
    Optional<QRCode> findByCellId(Long cellId);

    boolean existsByStorageObjectId(Long storageObjectId);
    Optional<QRCode> findByStorageObjectId(Long storageObjectId);
}
