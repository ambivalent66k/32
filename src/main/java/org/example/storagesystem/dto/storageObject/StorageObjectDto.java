package org.example.storagesystem.dto.storageObject;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.storagesystem.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StorageObjectDto {
    private Long id;

    @NotBlank(message = "it cannot be empty")
    @Size(min = 1, max = 50, message = "wrong size")
    private String type;

    @NotBlank(message = "it cannot be empty")
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

    @Min(value = 1, message = "must be bigger than 1")
    private int quantity;

    @Min(value = 1, message = "must be bigger than 1")
    private int volumePerUnit;

    private Status status;

    @Size(max = 500, message = "wrong size")
    private String notes;

    @NotNull
    private Long storageId;

    private Long cellId;

    private List<CustomAttributeDto> customAttributes;

    @NotNull(message = "it cannot be empty")
    private Long createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime updatedAt;
}
