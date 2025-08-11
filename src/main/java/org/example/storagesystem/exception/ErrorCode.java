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
