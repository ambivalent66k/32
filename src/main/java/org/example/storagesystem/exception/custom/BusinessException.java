package org.example.storagesystem.exception.custom;

import lombok.Getter;
import org.example.storagesystem.exception.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object[] args;

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(getFormattedMessage(errorCode, args));
        this.errorCode = errorCode;
        this.args = args;
    }

    private static String getFormattedMessage(ErrorCode code, Object[] args) {
        if (args == null || args.length == 0) {
            return code.getDefaultMessage();
        } else {
            return String.format(code.getDefaultMessage(), args);
        }
    }
}
