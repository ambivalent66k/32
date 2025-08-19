package org.example.storagesystem.service;

import org.example.storagesystem.dto.storage.StorageDto;
import org.example.storagesystem.dto.storage.StorageMoveDto;
import org.example.storagesystem.dto.storage.StoragePatchDto;
import org.example.storagesystem.dto.storage.response.StorageDtoResponse;
import org.example.storagesystem.dto.storage.response.StorageDtosResponse;
import org.springframework.data.domain.Page;

public interface StorageService {
    StorageDto createStorage(StorageDto storageDto);

    StorageDto updateStorage(StoragePatchDto storageDto, Long storageId);

    StorageDtoResponse findById(Long id);

    StorageDtoResponse moveStorage(StorageMoveDto storageMoveDto, Long storageId);

    Page<StorageDtosResponse> findAll(int page, int size);

    void deleteById(Long id);
}
