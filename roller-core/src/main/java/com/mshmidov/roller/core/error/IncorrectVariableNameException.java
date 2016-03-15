package com.mshmidov.roller.core.error;

public final class IncorrectVariableNameException extends AbnormalExitException {

    public IncorrectVariableNameException(String message) {
        super(message, ErrorCode.INCORRECT_VARIABLE_NAME.code);
    }

    public IncorrectVariableNameException(String message, Throwable cause) {
        super(message, cause, ErrorCode.INCORRECT_VARIABLE_NAME.code);
    }

    public IncorrectVariableNameException(Throwable cause) {
        super(cause, ErrorCode.INCORRECT_VARIABLE_NAME.code);
    }
}
