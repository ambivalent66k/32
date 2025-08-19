package org.example.storagesystem.dto.storageObject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.storagesystem.enums.AttributeType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomAttributeDto {
    @NotNull
    private AttributeType type;
    @NotBlank
    private String key;
    @NotBlank
    private String value;
}