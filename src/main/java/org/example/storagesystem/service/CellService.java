package org.example.storagesystem.service;

import org.example.storagesystem.dto.cell.CellDto;
import org.example.storagesystem.dto.cell.CellPatchDto;
import org.springframework.data.domain.Page;

public interface CellService {
    CellDto createCell(CellDto cellDto);

    CellDto updateCell(CellPatchDto cellDto, Long cellId);

    CellDto findWithChildrenById(Long id);

    Page<CellDto> findAll(int page, int size);

    void deleteById(Long id);
}
