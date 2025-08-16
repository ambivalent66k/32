package org.example.storagesystem.service;

import org.example.storagesystem.dto.storageObject.StorageObjectChangeQuantityDto;
import org.example.storagesystem.dto.storageObject.StorageObjectDto;
import org.example.storagesystem.dto.storageObject.StorageObjectMove;
import org.example.storagesystem.dto.storageObject.StorageObjectPatchDto;
import org.example.storagesystem.dto.storageObject.response.StorageObjectDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface StorageObjectService {

    StorageObjectDto createObject(StorageObjectDto storageObjectDto);

    StorageObjectDto updateObject(StorageObjectPatchDto storageObjectDto, Long id);

    @Transactional
    StorageObjectDto moveObject(StorageObjectMove storageObjectMove, Long id);

    StorageObjectDto changeQuantity(StorageObjectChangeQuantityDto changeQuantityDto, Long storageObjectId);

    StorageObjectDtoResponse findById(Long id);

    Page<StorageObjectDtoResponse> findAll(int page, int size);

    void deleteById(Long id);
}
