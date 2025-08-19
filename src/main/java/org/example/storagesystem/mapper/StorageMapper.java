package org.example.storagesystem.mapper;

import org.example.storagesystem.dto.storage.StorageDto;
import org.example.storagesystem.dto.storage.StorageMoveDto;
import org.example.storagesystem.dto.storage.StoragePatchDto;
import org.example.storagesystem.dto.storage.response.StorageDtoResponse;
import org.example.storagesystem.dto.storage.response.StorageDtosResponse;
import org.example.storagesystem.entity.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = {CellMapper.class, StorageObjectMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StorageMapper {
    @Mapping(target = "parentStorage", ignore = true)
    @Mapping(target = "cells", ignore = true)
    Storage mapTo(StorageDto storageDto);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "cells", ignore = true)
    @Mapping(source = "parentStorage.id", target = "parentStorageId")
    StorageDto mapTo(Storage storage);

    @Mapping(source = "children", target = "children")
    @Mapping(source = "parentStorage.id", target = "parentStorageId")
    StorageDtoResponse mapToDto(Storage storage);

    StorageDtosResponse mapToList(Storage storage);

    @Mapping(target = "parentStorage", ignore = true)
    @Mapping(target = "storageObjects", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "cells", ignore = true)
    Storage updateEntityFromDto(StoragePatchDto storagePatchDto, @MappingTarget Storage storage);

    Storage moveStorage(StorageMoveDto storageMoveDto, @MappingTarget Storage storage);
}
