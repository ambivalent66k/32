package org.example.storagesystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    STORAGE_NOT_FOUND("STORAGE_NOT_FOUND", "storage not found with id %s", HttpStatus.NOT_FOUND),
    STORAGE_CANNOT_BE_SELF(
            "STORAGE_CANNOT_BE_SELF", "storage cannot be self", HttpStatus.BAD_REQUEST),
    STORAGE_HAS_CYCLE("STORAGE_HAS_CYCLE", "storage has cycle", HttpStatus.BAD_REQUEST),
    STORAGE_IS_NOT_EMPTY("STORAGE_IS_NOT_EMPTY", "storage is not empty", HttpStatus.BAD_REQUEST),
    PARENT_CELL_NOT_FOUND("PARENT_CELL_NOT_FOUND", "parent cell not found", HttpStatus.BAD_REQUEST),
    PARENT_CELL_IN_OTHER_STORAGE(
            "PARENT_CELL_IN_OTHER_STORAGE",
            "parent cell in other storage",
            HttpStatus.BAD_REQUEST),
    CELL_NOT_FOUND("CELL_NOT_FOUND", "cell not found with id %s", HttpStatus.NOT_FOUND),
    CHOICE_ONE_CHANGE(
            "CHOICE_ONE_CHANGE", "cant change the storage and assign a parent at a time",
            HttpStatus.BAD_REQUEST
    ),
    PARENT_CELL_CANNOT_BE_SELF(
            "PARENT_CELL_CANNOT_BE_SELF",
            "parent cell cannot be self",
            HttpStatus.BAD_REQUEST
    ),
    CELL_IS_NOT_EMPTY("CELL_IS_NOT_EMPTY", "cell is not empty", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;

    ErrorCode(String code, String defaultMessage, HttpStatus status) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.status = status;
    }
}
