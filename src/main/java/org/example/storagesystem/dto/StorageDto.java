package org.example.storagesystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StorageDto {
    private Long id;

    @NotBlank(message = "it cannot be empty")
    @Size(min = 2, max = 50, message = "wrong size")
    private String name;

    @Size(max = 500, message = "wrong size")
    private String description;

    @NotBlank(message = "it cannot be empty")
    @Size(max = 255, message = "wrong size")
    private String location;

    private Long parentStorageId;

    private Long createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime updatedAt;

    private List<ChildStorageDto> children;
}
