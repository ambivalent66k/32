package org.example.storagesystem.dto.storage;

import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(hidden = true)
    private boolean parentStorageIdIsPresent;

    @JsonSetter("parent_storage_id")
    public void setStorageId(Long parentStorageId) {
        this.parentStorageId = parentStorageId;
        parentStorageIdIsPresent = true;
    }
}