package com.mshmidov.roller.core.error;

public final class IncorrectTableDefinitionException extends AbnormalExitException {

    private static final int CODE = 4;

    public IncorrectTableDefinitionException(String message) {
        super(message, CODE);
    }

    public IncorrectTableDefinitionException(String message, Throwable cause) {
        super(message, cause, CODE);
    }

    public IncorrectTableDefinitionException(Throwable cause) {
        super(cause, CODE);
    }
}
