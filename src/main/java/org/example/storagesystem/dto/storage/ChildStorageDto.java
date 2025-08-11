package org.example.storagesystem.dto.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChildStorageDto {
    private Long id;
    private String name;
    private String location;
}
