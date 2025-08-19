package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.storage.StorageDto;
import org.example.storagesystem.dto.storage.StorageMoveDto;
import org.example.storagesystem.dto.storage.response.StorageDtoResponse;
import org.example.storagesystem.dto.storage.response.StorageDtosResponse;
import org.example.storagesystem.dto.storage.StoragePatchDto;
import org.example.storagesystem.entity.Cell;
import org.example.storagesystem.entity.Storage;
import org.example.storagesystem.entity.StorageObject;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.mapper.StorageMapper;
import org.example.storagesystem.repository.CellRepository;
import org.example.storagesystem.repository.StorageObjectRepository;
import org.example.storagesystem.repository.StorageRepository;
import org.example.storagesystem.service.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final StorageMapper storageMapper;
    private final CellRepository cellRepository;
    private final StorageRepository storageRepository;
    private final StorageObjectRepository storageObjectRepository;

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

        storage = storageMapper.updateEntityFromDto(storageDto, storage);
        storageRepository.save(storage);

        return storageMapper.mapTo(storage);
    }

    @Override
    public StorageDtoResponse moveStorage(StorageMoveDto storageMoveDto, Long storageId) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, storageId));

        if (storageMoveDto.isParentStorageIdIsPresent()) {
            if (storageMoveDto.getParentStorageId() == null) {
                storage.setParentStorage(null);
            } else {
                if (!storageRepository.existsById(storageMoveDto.getParentStorageId())) {
                    throw new BusinessException(ErrorCode.STORAGE_NOT_FOUND);
                }

                if (storageId.equals(storageMoveDto.getParentStorageId())) {
                    throw new BusinessException(ErrorCode.STORAGE_CANNOT_BE_SELF);
                }

                if (storageRepository.hasCycle(storageId, storageMoveDto.getParentStorageId())) {
                    throw new BusinessException(ErrorCode.STORAGE_HAS_CYCLE);
                }

                Storage newParent = storageRepository.findById(storageMoveDto.getParentStorageId())
                        .orElseThrow(() -> new BusinessException(
                                ErrorCode.STORAGE_NOT_FOUND, storageMoveDto.getParentStorageId())
                        );

                storage.setParentStorage(newParent);
            }
        }

        storage = storageMapper.moveStorage(storageMoveDto, storage);
        storageRepository.save(storage);

        return storageMapper.mapToDto(storage);
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
    public StorageDtoResponse findById(Long id) {
        Storage storage = storageRepository.findWithChildrenById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, id));

        List<Cell> cells = cellRepository.findByStorageId(storage.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, id));

        List<StorageObject> storageObjects = storageObjectRepository.findByStorageId(storage.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_OBJECT_NOT_FOUND, id));

        storage.setCells(cells);
        storage.setStorageObjects(storageObjects);

        return storageMapper.mapToDto(storage);
    }

    @Override
    public Page<StorageDtosResponse> findAll(int page, int size) {
        Page<Storage> storages = storageRepository.findAll(PageRequest.of(page, size));
        return storages.map(storageMapper::mapToList);
    }
}
