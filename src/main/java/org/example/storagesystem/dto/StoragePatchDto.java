package org.example.storagesystem.dto;

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
public class StoragePatchDto {
    private Long id;

    @Size(min = 2, max = 50, message = "wrong size")
    private String name;

    @Size(max = 500, message = "wrong size")
    private String description;

    @Size(max = 255, message = "wrong size")
    private String location;

    private Long parentStorageId;

    private boolean parentStorageIdIsPresent;

    private Long createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime updatedAt;

    @JsonSetter("parentStorageId")
    public void setParentStorageId(Long parentStorageId) {
        this.parentStorageId = parentStorageId;
        this.parentStorageIdIsPresent = true;
    }
}
