package org.example.storagesystem.dto.cell;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CellDto {
    private Long id;

    @NotBlank(message = "it cannot be empty")
    @Size(min = 1, max = 50, message = "wrong size")
    private String name;

    @Size(max = 500, message = "wrong size")
    private String description;

    @NotBlank(message = "it cannot be empty")
    @Size(max = 255, message = "wrong size")
    private String location;

    @NotNull(message = "it cannot be empty")
    @Min(value = 1, message = "must be bigger than 1")
    private int capacity;

    @NotNull(message = "it cannot be empty")
    private Long storageId;

    private Long parentCellId;

    private Long createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime updatedAt;

    private List<ChildCellDto> children;
}
