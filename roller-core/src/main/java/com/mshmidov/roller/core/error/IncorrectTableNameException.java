package com.mshmidov.roller.core.error;

public final class IncorrectTableNameException extends AbnormalExitException {

    private static final int CODE = 3;

    public IncorrectTableNameException(String message) {
        super(message, CODE);
    }

    public IncorrectTableNameException(String message, Throwable cause) {
        super(message, cause, CODE);
    }

    public IncorrectTableNameException(Throwable cause) {
        super(cause, CODE);
    }
}
