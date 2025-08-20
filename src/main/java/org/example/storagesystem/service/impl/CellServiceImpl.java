package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.storagesystem.dto.cell.CellMoveDto;
import org.example.storagesystem.dto.cell.CellDto;
import org.example.storagesystem.dto.cell.CellPatchDto;
import org.example.storagesystem.dto.cell.response.CellDtoResponse;
import org.example.storagesystem.dto.cell.response.CellDtoResponseOnMove;
import org.example.storagesystem.dto.cell.response.CellDtosResponse;
import org.example.storagesystem.entity.Cell;
import org.example.storagesystem.entity.Storage;
import org.example.storagesystem.entity.StorageObject;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.mapper.CellMapper;
import org.example.storagesystem.repository.CellRepository;
import org.example.storagesystem.repository.StorageObjectRepository;
import org.example.storagesystem.repository.StorageRepository;
import org.example.storagesystem.service.CellService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CellServiceImpl implements CellService {
    private final CellMapper cellMapper;
    private final CellRepository cellRepository;
    private final StorageObjectRepository storageObjectRepository;
    private final StorageRepository storageRepository;
    
    @Override
    public CellDto createCell(CellDto cellDto) {
        Storage storage = storageRepository.findById(cellDto.getStorageId())
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, cellDto.getStorageId()));

        Cell parentCell;
        Cell cell = cellMapper.mapTo(cellDto);
        cell.setStorage(storage);

        if (cellDto.getParentCellId() != null) {
            parentCell = cellRepository.findById(cellDto.getParentCellId())
                    .orElseThrow(
                            () -> new BusinessException(ErrorCode.PARENT_CELL_NOT_FOUND, cellDto.getParentCellId())
                    );

            if (!Objects.equals(parentCell.getStorage().getId(), cellDto.getStorageId())) {
                throw new BusinessException(ErrorCode.PARENT_CELL_IN_OTHER_STORAGE, cellDto.getStorageId());
            }

            checkPlaceInCell(cell, parentCell);

            cell.setParentCell(parentCell);
        }

        cell.setCreatedBy(1L); // временно

        cellRepository.save(cell);

        return cellMapper.mapTo(cell);
    }

    @Override
    @Transactional
    public CellDto updateCell(CellPatchDto cellDto, Long cellId) {
        Cell cell = cellRepository.findWithObjectsById(cellId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, cellId));

        if (cellDto.getCapacity() != null) {
            List<StorageObject> storageObjects = cell.getStorageObjects();
            if (!storageObjects.isEmpty()) {
                checkOccupiedQuantity(cell, cellDto);
            }
        }

        cell = cellMapper.updateEntityFromDto(cellDto, cell);

        cellRepository.save(cell);

        return cellMapper.mapTo(cell);
    }

    @Override
    @Transactional
    public CellDtoResponseOnMove moveCell(CellMoveDto cellDto, Long cellId) {
        Cell cell = cellRepository.findWithStorage(cellId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, cellId));

        if (!cellDto.isStorageIdIsPresent() && !cellDto.isParentCellIdIsPresent()) {
            throw new BusinessException(ErrorCode.CANT_CHANGING_STORAGE_AND_CELL_AT_THE_SAME_TIME);
        }

        if (cellDto.isParentCellIdIsPresent()) {
            if (cellDto.getParentCellId() == null) {
                cell.setParentCell(null);
            }

            if (cellDto.getParentCellId() != null) {
                Cell parentCell = cellRepository.findWithStorage(cellDto.getParentCellId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.PARENT_CELL_NOT_FOUND));

                if (cellId.equals(cellDto.getParentCellId())) {
                    throw new BusinessException(ErrorCode.PARENT_CELL_CANNOT_BE_SELF);
                }

                if (storageRepository.hasCycle(cellId, cellDto.getParentCellId())) {
                    throw new BusinessException(ErrorCode.STORAGE_HAS_CYCLE);
                }

                checkPlaceInCell(cell, parentCell);

                transferringChildObjectsIfDifferentStorage(cell.getStorage().getId(), parentCell.getStorage().getId());

                cell.setParentCell(parentCell);
            }
        }

        if (cellDto.isStorageIdIsPresent()) {
            if (!storageRepository.existsById(cellDto.getStorageId())) {
                throw new BusinessException(ErrorCode.STORAGE_NOT_FOUND, cellDto.getStorageId());
            }

            transferringChildObjectsIfDifferentStorage(cell.getStorage().getId(), cellDto.getStorageId());

            Storage newStorage = storageRepository.getReferenceById(cellDto.getStorageId());
            cell.setStorage(newStorage);
        }

        cell = cellMapper.moveCell(cellDto, cell);
        cellRepository.save(cell);

        return cellMapper.mapToDtoOnMove(cell);
    }

    @Override
    public CellDtoResponse findById(Long id) {
        Cell cells = cellRepository.findWithChildrenById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, id));

        List<StorageObject> storageObjects = storageObjectRepository.findByCellId(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, id));

        cells.setStorageObjects(storageObjects);

        return cellMapper.mapToDto(cells);
    }

    @Override
    public Page<CellDtosResponse> findAll(int page, int size) {
        Page<Cell> cells = cellRepository.findAll(PageRequest.of(page, size));
        return cells.map(cellMapper::mapToList);
    }

    @Override
    public void deleteById(Long id) {
        Cell cell = cellRepository.findWithChildrenById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, id));

        if (!cell.getStorageObjects().isEmpty() || !cell.getChildren().isEmpty()) {
            throw new BusinessException(ErrorCode.CELL_IS_NOT_EMPTY);
        }

        cellRepository.delete(cell);
    }

    private void checkOccupiedQuantity(Cell cell, CellPatchDto cellDto) {
        int occupied = cell.getStorageObjects().stream()
                .mapToInt(obj -> obj.getQuantity() * obj.getVolumePerUnit())
                .sum();

        if (occupied > cellDto.getCapacity()) {
            throw new BusinessException(ErrorCode.NEW_CAPACITY_LESS_THEN_OCCUPIED);
        }
    }

    private void checkPlaceInCell(Cell cell, Cell parentCell) {
        if (parentCell.getCapacity() < cell.getCapacity()) {
            throw new BusinessException(ErrorCode.NOT_ENOUGH_CELL_CAPACITY);
        }

        int availableFromParent = parentCell.getCapacity() - parentCell.getStorageObjects().stream()
                .mapToInt(obj -> obj.getQuantity() * obj.getVolumePerUnit())
                .sum();

        if (availableFromParent < cell.getCapacity()) {
            throw new BusinessException(ErrorCode.NOT_ENOUGH_CELL_CAPACITY);
        }
    }

    private void transferringChildObjectsIfDifferentStorage(long oldStorageId, long newStorageId) {
        if (oldStorageId != newStorageId) {
            cellRepository.updateCellsStorage(oldStorageId, newStorageId);
            storageObjectRepository.updateStorageObjectsStorage(oldStorageId, newStorageId);
        }
    }
}
