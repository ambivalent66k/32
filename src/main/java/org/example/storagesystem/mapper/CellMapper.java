package org.example.storagesystem.mapper;

import org.example.storagesystem.dto.cell.CellDto;
import org.example.storagesystem.dto.cell.CellPatchDto;
import org.example.storagesystem.dto.cell.ChildCellDto;
import org.example.storagesystem.entity.Cell;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CellMapper {
    @Mapping(target = "storage", ignore = true)
    @Mapping(target = "parentCell", ignore = true)
    Cell mapTo(CellDto cellDto);

    @Mapping(source = "parentCell.id", target = "parentCellId")
    @Mapping(source = "storage.id", target = "storageId")
    CellDto mapTo(Cell cell);

    ChildCellDto toChildDto(Cell cell);

    List<ChildCellDto> toChildDtoList(List<Cell> children);

    @Mapping(target = "storage", ignore = true)
    @Mapping(target = "parentCell", ignore = true)
    Cell updateEntityFromDto(CellPatchDto cellPatchDto, @MappingTarget Cell cell);
}
