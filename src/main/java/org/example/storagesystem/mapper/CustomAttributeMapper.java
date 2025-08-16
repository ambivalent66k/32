package org.example.storagesystem.mapper;

import org.example.storagesystem.dto.storageObject.CustomAttributeDto;
import org.example.storagesystem.enums.AttributeType;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomAttributeMapper {
    default List<CustomAttributeDto> mapTo(Map<String, Object> customAttributes) {
        if (customAttributes == null) return null;

        return customAttributes.entrySet().stream()
                .map(e -> new CustomAttributeDto(AttributeType.STRING, e.getKey(),
                        String.valueOf(e.getValue())))
                .toList();
    }

    default Map<String, Object> mapTo(List<CustomAttributeDto> customAttributes) {
        if (customAttributes == null) return null;

        return customAttributes.stream()
                .collect(Collectors.toMap(CustomAttributeDto::getKey, CustomAttributeDto::getValue));
    }
}
