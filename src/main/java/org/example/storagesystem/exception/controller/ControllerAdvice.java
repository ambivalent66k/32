package org.example.storagesystem.exception.controller;

import org.example.storagesystem.exception.custom.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseOnError> handleBusinessException(BusinessException e) {
        ResponseOnError responseOnError = ResponseOnError.builder()
                .message(e.getMessage())
                .code(e.getErrorCode().getCode())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(
                responseOnError,
                e.getErrorCode().getStatus() != null ?
                        e.getErrorCode().getStatus() :
                        HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseOnError> handleBindException(BindException e) {
        List<ValidError> validErrors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String filed = ((FieldError) error).getField();
            String message = error.getDefaultMessage();

            validErrors.add(ValidError.builder()
                            .field(filed)
                            .message(message)
                            .code(message)
                            .build()
            );
        });

        ResponseOnError responseOnError = ResponseOnError.builder()
                .timestamp(LocalDateTime.now())
                .validErrors(validErrors)
                .build();

        return new ResponseEntity<>(responseOnError, HttpStatus.BAD_REQUEST);
    }
}
