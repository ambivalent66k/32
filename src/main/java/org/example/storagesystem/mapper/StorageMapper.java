package org.example.storagesystem.mapper;

import org.example.storagesystem.dto.ChildStorageDto;
import org.example.storagesystem.dto.StorageDto;
import org.example.storagesystem.dto.StoragePatchDto;
import org.example.storagesystem.entity.Storage;
import org.example.storagesystem.exception.ErrorCode;
import org.example.storagesystem.repository.StorageRepository;
import org.example.storagesystem.exception.custom.BusinessException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {StorageMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class StorageMapper {
    @Autowired
    protected StorageRepository storageRepository;

    @Mapping(target = "parentStorage", ignore = true)
    public abstract Storage mapTo(StorageDto storageDto);

    @Mapping(source = "parentStorage.id", target = "parentStorageId")
    public abstract StorageDto mapTo(Storage storage);

    public abstract List<ChildStorageDto> toChildDtoList(List<Storage> children);

    @Mapping(target = "parentStorage", ignore = true)
    public abstract Storage updateEntityFromDto(StoragePatchDto storagePatchDto, @MappingTarget Storage storage);

    @AfterMapping
    protected void setParentStorage(StoragePatchDto dto, @MappingTarget Storage storage) {
        if (dto.getParentStorageId() != null) {
            Storage parent = storageRepository.findById(dto.getParentStorageId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.STORAGE_NOT_FOUND, dto.getParentStorageId()));
            storage.setParentStorage(parent);
        }
    }
}
