package org.example.storagesystem.dto.cell;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CellPatchDto {
    private Long id;
    @Size(min = 1, max = 50, message = "wrong size")
    private String name;

    @Size(max = 500, message = "wrong size")
    private String description;

    @Size(min = 1, max = 255, message = "wrong size")
    private String location;

    private Integer capacity;

    private Long storageId;

    private Long parentCellId;

    private boolean parentCellIdIsPresent;

    private Long createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime updatedAt;

    @JsonSetter("parentCellId")
    public void setParentCellId(Long parentCellId) {
        this.parentCellId = parentCellId;
        this.parentCellIdIsPresent = true;
    }
}
