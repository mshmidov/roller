package com.mshmidov.roller.core.error;

public final class InternalErrorException extends AbnormalExitException {

    public InternalErrorException(String message) {
        super(message, ErrorCode.INTERNAL_ERROR.code);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(message, cause, ErrorCode.INTERNAL_ERROR.code);
    }

    public InternalErrorException(Throwable cause) {
        super(cause, ErrorCode.INTERNAL_ERROR.code);
    }
}
