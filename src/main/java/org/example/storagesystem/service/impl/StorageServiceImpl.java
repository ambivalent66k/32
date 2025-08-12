package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.storage.StorageDto;
import org.example.storagesystem.dto.storage.StoragePatchDto;
import org.example.storagesystem.entity.Storage;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.mapper.StorageMapper;
import org.example.storagesystem.repository.StorageRepository;
import org.example.storagesystem.service.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final StorageMapper storageMapper;
    private final StorageRepository storageRepository;

    @Override
    public StorageDto createStorage(StorageDto storageDto) {
        Storage storage = storageMapper.mapTo(storageDto);

        if (storageDto.getParentStorageId() != null) {
            Storage parentStorage = storageRepository.findById(storageDto.getParentStorageId())
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.STORAGE_NOT_FOUND, storageDto.getParentStorageId())
                    );
            storage.setParentStorage(parentStorage);
        }

        storageRepository.save(storage);

        return storageMapper.mapTo(storage);
    }

    @Override
    @Transactional
    public StorageDto updateStorage(StoragePatchDto storageDto, Long storageId) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, storageId));

        if (storageDto.isParentStorageIdIsPresent()) {
            if (storageDto.getParentStorageId() == null) {
                storage.setParentStorage(null);
            } else {
                if (!storageRepository.existsById(storageDto.getParentStorageId())) {
                    throw new BusinessException(ErrorCode.STORAGE_NOT_FOUND);
                }

                if (storageId.equals(storageDto.getParentStorageId())) {
                    throw new BusinessException(ErrorCode.STORAGE_CANNOT_BE_SELF);
                }

                if (storageRepository.hasCycle(storageId, storageDto.getParentStorageId())) {
                    throw new BusinessException(ErrorCode.STORAGE_HAS_CYCLE);
                }

                Storage newParent = storageRepository.findById(storageDto.getParentStorageId())
                        .orElseThrow(() -> new BusinessException(
                                ErrorCode.STORAGE_NOT_FOUND, storageDto.getParentStorageId())
                        );

                storage.setParentStorage(newParent);
            }
        }

        storage = storageMapper.updateEntityFromDto(storageDto, storage);
        storageRepository.save(storage);

        return storageMapper.mapTo(storage);
    }

    @Override
    public void deleteById(Long id) {
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, id)
                );

        if (!storage.getChildren().isEmpty()) {
            throw new BusinessException(ErrorCode.STORAGE_IS_NOT_EMPTY);
        }

        storageRepository.deleteById(id);
    }

    @Override
    public StorageDto findWithChildrenById(Long id) {
        Storage storage = storageRepository.findWithChildrenById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, id));

        return storageMapper.mapTo(storage);
    }

    @Override
    public Page<StorageDto> findAll(int page, int size) {
        Page<Storage> storages = storageRepository.findAllWithChildren(PageRequest.of(page, size));
        return storages.map(storageMapper::mapTo);
    }
}
