package org.example.storagesystem.service;

import org.example.storagesystem.dto.storage.StorageDto;
import org.example.storagesystem.dto.storage.StoragePatchDto;
import org.springframework.data.domain.Page;

public interface StorageService {
    StorageDto createStorage(StorageDto storageDto);

    StorageDto updateStorage(StoragePatchDto storageDto, Long storageId);

    StorageDto findWithChildrenById(Long id);

    Page<StorageDto> findAll(int page, int size);

    void deleteById(Long id);
}
