package com.mshmidov.roller.core.error;

public final class UnknownTableException extends AbnormalExitException {

    public UnknownTableException(String message) {
        super(message, ErrorCode.UNKNOWN_TABLE.code);
    }

    public UnknownTableException(String message, Throwable cause) {
        super(message, cause, ErrorCode.UNKNOWN_TABLE.code);
    }

    public UnknownTableException(Throwable cause) {
        super(cause, ErrorCode.UNKNOWN_TABLE.code);
    }
}
