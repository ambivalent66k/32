package org.example.storagesystem.mapper;

import org.example.storagesystem.dto.storageObject.StorageObjectDto;
import org.example.storagesystem.dto.storageObject.StorageObjectPatchDto;
import org.example.storagesystem.dto.storageObject.response.StorageObjectDtoResponse;
import org.example.storagesystem.entity.StorageObject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = {CustomAttributeMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StorageObjectMapper {
    @Mapping(source = "storage.id", target = "storageId")
    @Mapping(source = "cell.id", target = "cellId")
    @Mapping(source = "customAttributes", target = "customAttributes")
    StorageObjectDto mapTo(StorageObject entity);

//    @Mapping(target = "cell", ignore = true)
//    @Mapping(target = "storage", ignore = true)
    StorageObject mapTo(StorageObjectDto dto);

    StorageObjectDtoResponse toDto(StorageObject storageObject);

    @Mapping(target = "storage", ignore = true)
    @Mapping(target = "cell", ignore = true)
    StorageObject updateEntityFromDto(StorageObjectPatchDto storageObjectDto,
                                      @MappingTarget StorageObject storageObject);
}
