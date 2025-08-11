package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.StorageDto;
import org.example.storagesystem.dto.StoragePatchDto;
import org.example.storagesystem.entity.Storage;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.mapper.StorageMapper;
import org.example.storagesystem.repository.StorageRepository;
import org.example.storagesystem.service.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final StorageMapper storageMapper;
    private final StorageRepository storageRepository;

    @Override
    public StorageDto createStorage(StorageDto storageDto) {
        Storage storage;

        if (storageDto.getParentStorageId() != null &&
                storageRepository.existsById(storageDto.getParentStorageId())) {
            storage = storageMapper.mapTo(storageDto);
        } else if (storageDto.getParentStorageId() == null) {
            storage = storageMapper.mapTo(storageDto);
        } else {
            throw new BusinessException(ErrorCode.STORAGE_NOT_FOUND, storageDto.getParentStorageId());
        }

        storageRepository.save(storage);

        return storageMapper.mapTo(storage);
    }

    @Override
    public StorageDto updateStorage(StoragePatchDto storageDto, Long storageId) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, storageId));

        if (!storageDto.isParentStorageIdIsPresent()) {
            storage = storageMapper.updateEntityFromDto(storageDto, storage);
        } else if (storageDto.getParentStorageId() == null) {
            storage = storageMapper.updateEntityFromDto(storageDto, storage);
            storage.setParentStorage(null);
        } else if (storageRepository.existsById(storageDto.getParentStorageId())) {
            if (storageId.equals(storageDto.getParentStorageId())) {
                throw new BusinessException(ErrorCode.STORAGE_CANNOT_BE_SELF, storageDto.getParentStorageId());
            }

            if (storageRepository.hasCycle(storageId, storageDto.getParentStorageId())) {
                throw new BusinessException(ErrorCode.STORAGE_HAS_CYCLE, storageDto.getParentStorageId());
            }

            storage = storageMapper.updateEntityFromDto(storageDto, storage);
        } else {
            throw new BusinessException(ErrorCode.STORAGE_NOT_FOUND, storageDto.getParentStorageId());
        }

        storageRepository.save(storage);

        return storageMapper.mapTo(storage);
    }

    @Override
    public void deleteById(Long id) {
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, id)
        );

        if (!storage.getChildren().isEmpty()) {
            throw new BusinessException(ErrorCode.STORAGE_IS_NOT_EMPTY, id);
        }

        storageRepository.deleteById(id);
    }

    @Override
    public StorageDto findWithChildrenById(Long id) {
        Storage storage = storageRepository.findWithChildrenById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, id));

        StorageDto storageDto = storageMapper.mapTo(storage);
        storageDto.setChildren(storageMapper.toChildDtoList(storage.getChildren()));

        return storageDto;
    }

    @Override
    public Page<StorageDto> findAll(int page, int size) {
        Page<Storage> storages = storageRepository.findAll(PageRequest.of(page, size));
        return storages.map(storageMapper::mapTo);
    }
}
