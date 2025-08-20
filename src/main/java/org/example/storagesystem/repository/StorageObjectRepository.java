package org.example.storagesystem.repository;

import org.example.storagesystem.entity.StorageObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StorageObjectRepository extends JpaRepository<StorageObject, Long> {
    Optional<List<StorageObject>> findByStorageId(Long storageId);
    
    Optional<List<StorageObject>> findByCellId(Long cellId);
    
    @Modifying
    @Query("update StorageObject so set so.storage.id = :newStorageId where so.storage.id = :oldStorageId")
    void updateStorageObjectsStorage(@Param("oldStorageId") Long oldStorageId, @Param("newStorageId") Long newStorageId);
    
    int countByName(String name);
}
