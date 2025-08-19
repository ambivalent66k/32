package org.example.storagesystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    STORAGE_NOT_FOUND("STORAGE_NOT_FOUND", "storage not found with id %s", HttpStatus.NOT_FOUND),
    STORAGE_CANNOT_BE_SELF(
            "STORAGE_CANNOT_BE_SELF",
            "storage cannot be self",
            HttpStatus.BAD_REQUEST),
    STORAGE_HAS_CYCLE("STORAGE_HAS_CYCLE", "storage has cycle", HttpStatus.BAD_REQUEST),
    STORAGE_IS_NOT_EMPTY("STORAGE_IS_NOT_EMPTY", "storage is not empty", HttpStatus.CONFLICT),
    PARENT_CELL_NOT_FOUND("PARENT_CELL_NOT_FOUND", "parent cell not found", HttpStatus.BAD_REQUEST),
    PARENT_CELL_IN_OTHER_STORAGE(
            "PARENT_CELL_IN_OTHER_STORAGE",
            "parent cell in other storage",
            HttpStatus.BAD_REQUEST),
    CELL_IN_OTHER_STORAGE(
            "CELL_IN_OTHER_STORAGE",
            "cell in other storage",
            HttpStatus.BAD_REQUEST),
    CELL_NOT_FOUND("CELL_NOT_FOUND", "cell not found with id %s", HttpStatus.NOT_FOUND),
    CHOICE_ONE_CHANGE(
            "CHOICE_ONE_CHANGE", "cant change the storage and assign a parent at a time",
            HttpStatus.BAD_REQUEST),
    PARENT_CELL_CANNOT_BE_SELF(
            "PARENT_CELL_CANNOT_BE_SELF",
            "parent cell cannot be self",
            HttpStatus.BAD_REQUEST),
    CELL_IS_NOT_EMPTY("CELL_IS_NOT_EMPTY", "cell is not empty", HttpStatus.CONFLICT),
    NEW_CAPACITY_LESS_THEN_OCCUPIED(
            "NEW_CAPACITY_LESS_THEN_OCCUPIED",
            "new capacity less then occupied",
            HttpStatus.BAD_REQUEST),
    STORAGE_OBJECT_NOT_FOUND(
            "STORAGE_OBJECT_NOT_FOUND",
            "object not found with id %s",
            HttpStatus.NOT_FOUND),
    TYPE_MUST_NOT_BE_EMPTY(
            "TYPE_MUST_NOT_BE_EMPTY",
            "object type must not be empty",
            HttpStatus.BAD_REQUEST),
    NAME_MUST_NOT_BE_EMPTY(
            "NAME_MUST_NOT_BE_EMPTY",
            "object name must not be empty",
            HttpStatus.BAD_REQUEST),
    CANT_CHANGING_STORAGE_AND_CELL_AT_THE_SAME_TIME(
            "CANT_CHANGING_STORAGE_AND_CELL_AT_THE_SAME_TIME",
            "cant change storage and cell at the same time",
            HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_CELL_CAPACITY(
            "NOT_ENOUGH_CELL_CAPACITY",
            "not enough capacity",
            HttpStatus.CONFLICT),
    STORAGE_OBJECT_NOT_ENOUGH(
            "STORAGE_OBJECT_NOT_ENOUGH",
            "storage object not enough",
            HttpStatus.CONFLICT),
    PHOTO_NOT_UPLOADED("PHOTO_NOT_UPLOADED", "photo not uploaded", HttpStatus.BAD_REQUEST),
    PHOTO_NOT_DELETED("PHOTO_NOT_DELETED", "photo not deleted", HttpStatus.BAD_REQUEST),
    WRONG_TYPE_OF_ATTRIBUTE(
            "WRONG_TYPE_OF_ATTRIBUTE",
            "wrong type of attribute",
            HttpStatus.BAD_REQUEST),
    QUANTITY_CANNOT_BE_NEGATIVE(
            "QUANTITY_CANNOT_BE_NEGATIVE",
            "quantity cannot be negative",
            HttpStatus.BAD_REQUEST),
    QRCODE_NOT_FOUNT("QRCODE_NOT_FOUND", "qrcode not found", HttpStatus.NOT_FOUND),
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
