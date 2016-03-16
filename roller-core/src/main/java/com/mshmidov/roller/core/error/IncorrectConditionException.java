package com.mshmidov.roller.core.error;

public final class IncorrectConditionException extends AbnormalExitException {

    public IncorrectConditionException(String message) {
        super(message, ErrorCode.INCORRECT_CONDITION.code);
    }

    public IncorrectConditionException(String message, Throwable cause) {
        super(message, cause, ErrorCode.INCORRECT_CONDITION.code);
    }

    public IncorrectConditionException(Throwable cause) {
        super(cause, ErrorCode.INCORRECT_CONDITION.code);
    }
}
