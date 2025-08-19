package org.example.storagesystem.dto.storageObject.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.storagesystem.dto.storageObject.CustomAttributeDto;
import org.example.storagesystem.enums.object.Status;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StorageObjectDtoResponse {
    private Long id;
    private String name;
    private String type;
    private Status status;
    private int quantity;
    private List<CustomAttributeDto> customAttributes;
}
