package org.example.storagesystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.storagesystem.dto.cell.CellDto;
import org.example.storagesystem.dto.cell.CellPatchDto;
import org.example.storagesystem.entity.Cell;
import org.example.storagesystem.entity.Storage;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.exception.custom.BusinessException;
import org.example.storagesystem.mapper.CellMapper;
import org.example.storagesystem.repository.CellRepository;
import org.example.storagesystem.repository.StorageRepository;
import org.example.storagesystem.service.CellService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CellServiceImpl implements CellService {
    private final CellMapper cellMapper;
    private final CellRepository cellRepository;
    private final StorageRepository storageRepository;

    @Override
    public CellDto createCell(CellDto cellDto) {
        Storage storage = storageRepository.findById(cellDto.getStorageId())
                .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, cellDto.getStorageId()));

        Cell parentCell = null;
        Cell cell = cellMapper.mapTo(cellDto);

        if (cellDto.getParentCellId() != null) {
            parentCell = cellRepository.findById(cellDto.getParentCellId())
                    .orElseThrow(
                            () -> new BusinessException(ErrorCode.PARENT_CELL_NOT_FOUND, cellDto.getParentCellId())
                    );

            if (!Objects.equals(parentCell.getStorage().getId(), cellDto.getStorageId())) {
                throw new BusinessException(ErrorCode.PARENT_CELL_IN_OTHER_STORAGE, cellDto.getStorageId());
            }
        }

        cell.setStorage(storage);
        cell.setParentCell(parentCell);

        cellRepository.save(cell);

        return cellMapper.mapTo(cell);
    }

    @Override
    @Transactional
    public CellDto updateCell(CellPatchDto cellDto, Long cellId) {
        Cell cell = cellRepository.findWithChildrenById(cellId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, cellId));

//        if (cellDto.getCapacity() != null) { проверка свободного места
//        }

        if (cellDto.getStorageId() != null) {
            if (cellDto.getParentCellId() != null) {
                throw new BusinessException(ErrorCode.CHOICE_ONE_CHANGE);
            }

            Storage storage = storageRepository.findById(cellDto.getStorageId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, cellDto.getStorageId()));
            cell.setParentCell(null);
            cell.setStorage(storage);
            changeCellForChildren(cell, storage);
        }

        if (cellDto.isParentCellIdIsPresent()) {
            if (cellDto.getParentCellId() == null) {
                cell.setParentCell(null);
            } else {
                if (!cellRepository.existsById(cellDto.getParentCellId())) {
                    throw new BusinessException(ErrorCode.PARENT_CELL_NOT_FOUND, cellDto.getParentCellId());
                }

                if (cellId.equals(cellDto.getParentCellId())) {
                    throw new BusinessException(ErrorCode.PARENT_CELL_CANNOT_BE_SELF);
                }

                if (storageRepository.hasCycle(cellId, cellDto.getParentCellId())) {
                    throw new BusinessException(ErrorCode.STORAGE_HAS_CYCLE);
                }

                //if () проверка вместимости родителя

                Cell newParentCell = cellRepository.findById(cellDto.getParentCellId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, cellDto.getParentCellId()));

                cell.setParentCell(newParentCell);

                if (!cell.getStorage().getId().equals(cellDto.getStorageId())) {
                    Storage newStorage = newParentCell.getStorage();
                    cell.setStorage(newStorage);
                    changeCellForChildren(cell, newStorage);
                }
            }
        }

        cell = cellMapper.updateEntityFromDto(cellDto, cell);

        cellRepository.save(cell);

        return cellMapper.mapTo(cell);
    }

    private void changeCellForChildren(Cell cell, Storage storage) {
        cell.getChildren().forEach(child -> child.setStorage(storage));
    }

    @Override
    public CellDto findWithChildrenById(Long id) {
        return cellRepository.findWithChildrenById(id).
                map(cellMapper::mapTo)
                .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, id));
    }

    @Override
    public Page<CellDto> findAll(int page, int size) {
        Page<Cell> cells = cellRepository.findAllWithChildren(PageRequest.of(page, size));
        return cells.map(cellMapper::mapTo);
    }

    @Override
    public void deleteById(Long id) {
        Cell cell = cellRepository.findWithChildrenById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CELL_NOT_FOUND, id));

        if (cell.getChildren() != null) {
            Cell child = cell.getChildren().getFirst();
            if (child.getChildren() != null) {
                throw new BusinessException(ErrorCode.CELL_IS_NOT_EMPTY);
            }
        }

        cellRepository.delete(cell);
    }
}
