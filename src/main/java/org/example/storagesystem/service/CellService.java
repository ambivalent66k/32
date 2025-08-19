package org.example.storagesystem.service;

import org.example.storagesystem.dto.cell.CellDto;
import org.example.storagesystem.dto.cell.CellMoveDto;
import org.example.storagesystem.dto.cell.CellPatchDto;
import org.example.storagesystem.dto.cell.response.CellDtoResponse;
import org.example.storagesystem.dto.cell.response.CellDtoResponseOnMove;
import org.example.storagesystem.dto.cell.response.CellDtosResponse;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface CellService {
    CellDto createCell(CellDto cellDto);

    CellDto updateCell(CellPatchDto cellDto, Long cellId);

    @Transactional
    CellDtoResponseOnMove moveCell(CellMoveDto cellDto, Long cellId);

    CellDtoResponse findById(Long id);

    Page<CellDtosResponse> findAll(int page, int size);

    void deleteById(Long id);
}
