package org.example.storagesystem.mapper;

import org.example.storagesystem.dto.cell.CellDto;
import org.example.storagesystem.dto.cell.CellMoveDto;
import org.example.storagesystem.dto.cell.CellPatchDto;
import org.example.storagesystem.dto.cell.response.CellDtoResponse;
import org.example.storagesystem.dto.cell.response.CellDtoResponseOnMove;
import org.example.storagesystem.dto.cell.response.CellDtosResponse;
import org.example.storagesystem.entity.Cell;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = {CustomAttributeMapper.class, StorageObjectMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CellMapper {
    @Mapping(target = "storage", ignore = true)
    @Mapping(target = "parentCell", ignore = true)
    Cell mapTo(CellDto cellDto);

    @Mapping(source = "storage.id", target = "storageId")
    @Mapping(source = "parentCell.id", target = "parentCellId")
    @Mapping(target = "children", ignore = true)
    CellDto mapTo(Cell cell);

    @Mapping(source = "storage.id", target = "storageId")
    @Mapping(source = "parentCell.id", target = "parentCellId")
    @Mapping(source = "storageObjects", target = "storageObjects")
    CellDtoResponse mapToDto(Cell cell);

    @Mapping(target = "storageId", source = "storage.id")
    @Mapping(target = "parentCellId", source = "parentCell.id")
    CellDtoResponseOnMove mapToDtoOnMove(Cell cell);

    @Mapping(target = "storageId", source = "storage.id")
    CellDtosResponse mapToList(Cell cell);

    @Mapping(target = "storage", ignore = true)
    @Mapping(target = "parentCell", ignore = true)
    Cell updateEntityFromDto(CellPatchDto cellPatchDto, @MappingTarget Cell cell);

    Cell moveCell(CellMoveDto cellMoveDto, @MappingTarget Cell cell);
}
