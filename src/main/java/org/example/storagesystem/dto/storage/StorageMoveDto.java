package org.example.storagesystem.dto.storage;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StorageMoveDto {
    @NotNull
    private Long parentStorageId;

    private String reason;

    private boolean parentStorageIdIsPresent;

    @JsonSetter("parentStorageId")
    public void setStorageId(Long parentStorageId) {
        this.parentStorageId = parentStorageId;
        parentStorageIdIsPresent = true;
    }
}