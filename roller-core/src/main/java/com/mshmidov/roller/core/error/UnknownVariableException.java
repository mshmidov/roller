package com.mshmidov.roller.core.error;

public final class UnknownVariableException extends AbnormalExitException {

    public UnknownVariableException(String message) {
        super(message, ErrorCode.UNKNOWN_VARIABLE.code);
    }

    public UnknownVariableException(String message, Throwable cause) {
        super(message, cause, ErrorCode.UNKNOWN_VARIABLE.code);
    }

    public UnknownVariableException(Throwable cause) {
        super(cause, ErrorCode.UNKNOWN_VARIABLE.code);
    }
}
