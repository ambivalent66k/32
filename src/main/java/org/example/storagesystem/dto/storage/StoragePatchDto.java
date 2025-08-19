package org.example.storagesystem.dto.storage;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoragePatchDto {
    @Size(min = 2, max = 50, message = "wrong size")
    private String name;

    @Size(max = 500, message = "wrong size")
    private String description;

    @Size(min = 1, max = 255, message = "wrong size")
    private String location;
}
