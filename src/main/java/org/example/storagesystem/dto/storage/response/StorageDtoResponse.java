package org.example.storagesystem.dto.storage.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.storagesystem.dto.cell.response.CellDtosResponse;
import org.example.storagesystem.dto.storage.ChildStorageDto;
import org.example.storagesystem.dto.storageObject.response.StorageObjectDtoResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StorageDtoResponse {
    private Long id;

    private String name;

    private String description;

    private String location;

    private Long parentStorageId;

    private Long createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<StorageObjectDtoResponse> storageObjects;

    private List<CellDtosResponse> cells;

    private List<ChildStorageDto> children;
}
