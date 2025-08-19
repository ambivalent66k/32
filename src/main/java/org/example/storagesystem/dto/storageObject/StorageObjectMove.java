package org.example.storagesystem.dto.storageObject;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StorageObjectMove {
    private Long storageId;

    private Long cellId;

    private String reason;

    private boolean storageIdIsPresent;

    private boolean cellIdIsPresent;

    @JsonSetter("storageId")
    public void setStorageId(Long storageId) {
        this.storageId = storageId;
        this.storageIdIsPresent = true;
    }

    @JsonSetter("cellId")
    public void setCellId(Long cellId) {
        this.cellId = cellId;
        this.cellIdIsPresent = true;
    }
}
