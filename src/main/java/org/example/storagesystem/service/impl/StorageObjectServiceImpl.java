package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.storageObject.*;
import org.example.storagesystem.dto.storageObject.response.StorageObjectDtoResponse;
import org.example.storagesystem.entity.Cell;
import org.example.storagesystem.entity.Storage;
import org.example.storagesystem.entity.StorageObject;
import org.example.storagesystem.enums.AttributeType;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.mapper.StorageObjectMapper;
import org.example.storagesystem.repository.CellRepository;
import org.example.storagesystem.repository.StorageObjectRepository;
import org.example.storagesystem.repository.StorageRepository;
import org.example.storagesystem.service.StorageObjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StorageObjectServiceImpl implements StorageObjectService {
    private final CellRepository cellRepository;
    private final StorageObjectMapper storageObjectMapper;
    private final StorageRepository storageRepository;
    private final StorageObjectRepository storageObjectRepository;

    @Override
    public StorageObjectDto createObject(StorageObjectDto storageObjectDto) {
        Storage storage = storageRepository.findById(storageObjectDto.getStorageId())
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, storageObjectDto.getStorageId()));

        StorageObject storageObject = storageObjectMapper.mapTo(storageObjectDto);
        storageObject.setStorage(storage);

        if (storageObjectDto.getCellId() != null) {
            Cell cell = cellRepository.findById(storageObjectDto.getCellId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, storageObjectDto.getCellId()));

            if (!cell.getStorage().getId().equals(storage.getId())) {
                throw new BusinessException(ErrorCode.CELL_IN_OTHER_STORAGE, storageObjectDto.getCellId());
            }

            checkPlaceInCell(cell, storageObjectDto.getQuantity(), storageObjectDto.getVolumePerUnit());

            storageObject.setCell(cell);
        }

        if (storageObjectDto.getCustomAttributes() != null) {
            Map<String, Object> customMap = new HashMap<>();

            for (CustomAttributeDto cad : storageObjectDto.getCustomAttributes()) {
                Object parsed = parseByType(cad.getType(), cad.getValue());
                customMap.put(cad.getKey(), parsed);
            }

            storageObject.setCustomAttributes(customMap);
        }

        storageObjectRepository.save(storageObject);

        return storageObjectMapper.mapTo(storageObject);
    }

    @Override
    @Transactional
    public StorageObjectDto updateObject(StorageObjectPatchDto storageObjectDto, Long id) {
        StorageObject storageObject = storageObjectRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_OBJECT_NOT_FOUND, id));

        if (storageObjectDto.getVolumePerUnit() != null && storageObject.getCell() != null) {
            Cell cell = cellRepository.findWithObjectsById(storageObject.getCell().getId())
                            .orElseThrow(() -> new BusinessException(
                                    ErrorCode.CELL_NOT_FOUND,
                                    storageObjectDto.getCellId()));

            checkOccupiedQuantity(cell, storageObject, storageObjectDto);
        }

        storageObject = storageObjectMapper.updateEntityFromDto(storageObjectDto, storageObject);
        storageObjectRepository.save(storageObject);

        return storageObjectMapper.mapTo(storageObject);
    }


    @Override
    @Transactional
    public StorageObjectDto moveObject(StorageObjectMove storageObjectMove, Long id) {
        StorageObject storageObject = storageObjectRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_OBJECT_NOT_FOUND, id));

        if (storageObjectMove.isStorageIdIsPresent() && storageObjectMove.isCellIdIsPresent()) {
            throw new BusinessException(
                    ErrorCode.CANT_CHANGING_STORAGE_AND_CELL_AT_THE_SAME_TIME,
                    storageObjectMove.getCellId());
        }

        if (storageObjectMove.isCellIdIsPresent()) {
            Cell cell = cellRepository.findWithObjectsById(storageObjectMove.getCellId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, storageObjectMove.getCellId()));

            checkOccupiedQuantity(cell, storageObject);

            storageObject.setStorage(cell.getStorage());
            storageObject.setCell(cell);
        }

        if (storageObjectMove.isStorageIdIsPresent()) {
            if (storageObjectMove.getStorageId() == null) {
                storageObject.setStorage(null);
                storageObject.setCell(null);
            } else {
                Storage storage = storageRepository.findById(storageObjectMove.getStorageId())
                        .orElseThrow(() -> new BusinessException(
                                ErrorCode.STORAGE_NOT_FOUND,
                                storageObjectMove.getStorageId()));

                storageObject.setStorage(storage);
                storageObject.setCell(null);
            }
        }
        storageObjectRepository.save(storageObject);

        return storageObjectMapper.mapTo(storageObject);
    }

    @Override
    public StorageObjectDto changeQuantity(StorageObjectChangeQuantityDto changeQuantityDto, Long storageObjectId) {
        StorageObject storageObject = storageObjectRepository.findById(storageObjectId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_OBJECT_NOT_FOUND, storageObjectId));

        int resultQuantity = 0;

        if (changeQuantityDto.getAction().name().equals("DECREMENT")) {
            resultQuantity = storageObject.getQuantity() - changeQuantityDto.getDelta();
        } else if (changeQuantityDto.getAction().name().equals("INCREMENT")) {
            resultQuantity = storageObject.getQuantity() + changeQuantityDto.getDelta();
        }

        if (resultQuantity < 0) {
            throw new BusinessException(ErrorCode.QUANTITY_CANNOT_BE_NEGATIVE);
        }

        if (storageObject.getCell() != null && changeQuantityDto.getAction().name().equals("INCREMENT")) { // //
            int occupiedQuantity = occupiedQuantity(storageObject.getCell());
            int availableQuantity = storageObject.getQuantity() - occupiedQuantity;

            int required = resultQuantity * storageObject.getVolumePerUnit();

            if (required <= availableQuantity) {
                throw new BusinessException(ErrorCode.NOT_ENOUGH_CELL_CAPACITY);
            }
        }

        storageObject.setQuantity(resultQuantity);
        storageObjectRepository.save(storageObject);

        return storageObjectMapper.mapTo(storageObject);
    }

    @Override
    public StorageObjectDtoResponse findById(Long id) {
        StorageObject storageObject = storageObjectRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, id));

        return storageObjectMapper.toDto(storageObject);
    }

    @Override
    public Page<StorageObjectDtoResponse> findAll(int page, int size) {
        Page<StorageObject> storageObjects = storageObjectRepository.findAll(PageRequest.of(page, size));

        return storageObjects.map(storageObjectMapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        StorageObject storageObject = storageObjectRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, id));

        if (storageObject.getQuantity() > 0) {
            throw new BusinessException(ErrorCode.STORAGE_OBJECT_NOT_ENOUGH);
        }

        storageObjectRepository.delete(storageObject);
    }

    private void checkPlaceInCell(Cell cell, int quantity, int volumePerUnit) {
        int occupied = cell.getStorageObjects().stream()
                .mapToInt(obj -> obj.getQuantity() * obj.getVolumePerUnit())
                .sum();

        int required = quantity * volumePerUnit;
        int available = cell.getCapacity() - occupied;

        if (required > available) {
            throw new BusinessException(ErrorCode.NOT_ENOUGH_CELL_CAPACITY);
        }
    }

    private Object parseByType(AttributeType type, String value) {
        return switch (type) {
            case STRING, FILE_URL -> value;
            case INTEGER -> Integer.parseInt(value);
            case FLOAT -> Double.parseDouble(value);
            case BOOLEAN -> Boolean.parseBoolean(value);
            case DATE -> LocalDate.parse(value);
            default -> throw new BusinessException(ErrorCode.WRONG_TYPE_OF_ATTRIBUTE);
        };
    }

    private int occupiedQuantity(Cell cell) {
        return cell.getStorageObjects().stream()
                .mapToInt(obj -> obj.getQuantity() * obj.getVolumePerUnit())
                .sum();
    }

    private void checkOccupiedQuantity(Cell cell, StorageObject storageObject, StorageObjectPatchDto storageObjectDto) {
        int occupiedQuantity = occupiedQuantity(cell);

        int oldQuantity = storageObject.getQuantity() * storageObject.getVolumePerUnit();
        int occupiedQuantityWithoutObject = occupiedQuantity - oldQuantity;

        int requiredNow = storageObjectDto.getQuantity() * storageObject.getVolumePerUnit();

        int availableQuantity = cell.getCapacity() - occupiedQuantityWithoutObject;

        if (requiredNow > availableQuantity) {
            throw new BusinessException(ErrorCode.NOT_ENOUGH_CELL_CAPACITY);
        }
    }

    private void checkOccupiedQuantity(Cell cell, StorageObject storageObject) {
        int occupiedQuantity = occupiedQuantity(cell);

        int availableQuantity = cell.getCapacity() - occupiedQuantity;
        int required = storageObject.getQuantity() * storageObject.getVolumePerUnit();

        if (required > availableQuantity) {
            throw new BusinessException(ErrorCode.NOT_ENOUGH_CELL_CAPACITY);
        }

    }
}













