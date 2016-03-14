package com.mshmidov.roller.cli.error;

public final class InternalErrorException extends AbnormalExitException {

    private static final int CODE = 99;

    public InternalErrorException(String message) {
        super(message, CODE);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(message, cause, CODE);
    }

    public InternalErrorException(Throwable cause) {
        super(cause, CODE);
    }
}
