package com.mshmidov.roller.core.error;

public final class IncorrectDiceExpressionException extends AbnormalExitException {

    public IncorrectDiceExpressionException(String message) {
        super(message, ErrorCode.INCORRECT_DICE_EXPRESSION.code);
    }

    public IncorrectDiceExpressionException(String message, Throwable cause) {
        super(message, cause, ErrorCode.INCORRECT_DICE_EXPRESSION.code);
    }

    public IncorrectDiceExpressionException(Throwable cause) {
        super(cause, ErrorCode.INCORRECT_DICE_EXPRESSION.code);
    }
}
