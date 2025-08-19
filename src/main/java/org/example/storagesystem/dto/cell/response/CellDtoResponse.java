package org.example.storagesystem.dto.cell.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.storagesystem.dto.cell.ChildCellDto;
import org.example.storagesystem.dto.storageObject.response.StorageObjectDtoResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CellDtoResponse {
    private Long id;
    private String name;
    private String location;
    private int capacity;
    private List<StorageObjectDtoResponse> storageObjects;
    private List<ChildCellDto> children;
}
