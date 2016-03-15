package com.mshmidov.roller.core.error;

public final class IncorrectDiceExpressionException extends AbnormalExitException {

    private static final int CODE = 2;

    public IncorrectDiceExpressionException(String message) {
        super(message, CODE);
    }

    public IncorrectDiceExpressionException(String message, Throwable cause) {
        super(message, cause, CODE);
    }

    public IncorrectDiceExpressionException(Throwable cause) {
        super(cause, CODE);
    }
}
