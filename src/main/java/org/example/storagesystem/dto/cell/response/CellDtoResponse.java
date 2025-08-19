package org.example.storagesystem.dto.cell.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.storagesystem.dto.cell.ChildCellDto;
import org.example.storagesystem.dto.storageObject.response.StorageObjectDtoResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CellDtoResponse {
    private Long id;

    private String name;

    private String location;

    private String description;

    private Long createdBy;

    private int capacity;

    private Long storageId;

    private Long parentCellId;

    private List<StorageObjectDtoResponse> storageObjects;

    private List<ChildCellDto> children;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime updatedAt;
}
