package org.example.storagesystem.dto.storageObject.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.storagesystem.enums.object.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

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

    private String description;

    private String photoUrl;

    private LocalDate purchaseDate;

    private String purchaseLocation;

    private int volumePerUnit;

    private String notes;

    private Long storageId;

    private Long cellId;

    private Map<String, Object> customAttributes;

    private Long createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime updatedAt;
}
