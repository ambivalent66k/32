package org.example.storagesystem.dto.storageObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.storagesystem.enums.Action;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StorageObjectChangeQuantityDto {
    private Action action;
    private int delta;
    private String reason;
}
