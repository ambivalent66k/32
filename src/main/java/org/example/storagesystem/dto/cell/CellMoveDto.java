package org.example.storagesystem.dto.cell;


import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CellMoveDto {
    private Long storageId;

    private Long parentCellId;

    private String reason;

    private boolean storageIdIsPresent;

    private boolean parentCellIdIsPresent;

    @JsonSetter("storage_id")
    public void setStorageId(Long storageId) {
        this.storageId = storageId;
        this.storageIdIsPresent = true;;
    }

    @JsonSetter("parent_cell_id")
    public void setParentCellId(Long parentCellId) {
        this.parentCellId = parentCellId;
        this.parentCellIdIsPresent = true;
    }
}
