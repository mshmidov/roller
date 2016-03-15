package com.mshmidov.roller.core.error;

public final class IncorrectTableDefinitionException extends AbnormalExitException {

    public IncorrectTableDefinitionException(String message) {
        super(message, ErrorCode.INCORRECT_TABLE_DEFINITION.code);
    }

    public IncorrectTableDefinitionException(String message, Throwable cause) {
        super(message, cause, ErrorCode.INCORRECT_TABLE_DEFINITION.code);
    }

    public IncorrectTableDefinitionException(Throwable cause) {
        super(cause, ErrorCode.INCORRECT_TABLE_DEFINITION.code);
    }
}
