package org.example.storagesystem.dto.storageObject;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.storagesystem.enums.object.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StorageObjectPatchDto {
    private Long id;

    @Size(min = 1, max = 50, message = "wrong size")
    private String type;

    @Size(min = 1, max = 50, message = "wrong size")
    private String name;

    @Size(max = 500, message = "wrong size")
    private String description;

    @Column(name = "photo_url")
    @Size(max = 500, message = "wrong size")
    private String photoUrl;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_location")
    @Size(max = 255, message = "wrong size")
    private String purchaseLocation;

    private int quantity;

    private Integer volumePerUnit;

    private Status status = Status.ACTIVE;

    @Size(max = 500, message = "wrong size")
    private String notes;

    private Long storageId;

    private Long cellId;

    @Valid
    private List<CustomAttributeDto> customAttributes;

    private Long createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime updatedAt;
}
