package org.example.storagesystem.mapper;

import org.example.storagesystem.dto.storage.ChildStorageDto;
import org.example.storagesystem.dto.storage.StorageDto;
import org.example.storagesystem.dto.storage.StoragePatchDto;
import org.example.storagesystem.entity.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {CellMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StorageMapper {
    @Mapping(target = "parentStorage", ignore = true)
    Storage mapTo(StorageDto storageDto);

    @Mapping(source = "children", target = "children")
    @Mapping(source = "parentStorage.id", target = "parentStorageId")
    @Mapping(target = "cells", ignore = true)
    StorageDto mapTo(Storage storage);

    ChildStorageDto toChildDto(Storage cell);

    List<ChildStorageDto> toChildDtoList(List<Storage> children);

    @Mapping(target = "parentStorage", ignore = true)
    Storage updateEntityFromDto(StoragePatchDto storagePatchDto, @MappingTarget Storage storage);
}
