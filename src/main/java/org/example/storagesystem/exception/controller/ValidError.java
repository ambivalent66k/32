package org.example.storagesystem.exception.controller;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ValidError {
    private String field;
    private String message;
    private String code;
}
