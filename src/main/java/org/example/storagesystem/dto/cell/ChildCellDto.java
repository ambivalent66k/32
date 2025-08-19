package org.example.storagesystem.dto.cell;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChildCellDto {
    private Long id;
    private String name;
    private String location;
}
