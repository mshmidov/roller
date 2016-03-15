package com.mshmidov.roller.core.error;

public final class IncorrectUsageException extends AbnormalExitException {

    private final String usage;

    public IncorrectUsageException(String message, String usage) {
        super(message, ErrorCode.INCORRECT_USAGE.code);
        this.usage = usage;
    }

    public IncorrectUsageException(String message, Throwable cause, String usage) {
        super(message, cause, ErrorCode.INCORRECT_USAGE.code);
        this.usage = usage;
    }

    public IncorrectUsageException(Throwable cause, String usage) {
        super(cause, ErrorCode.INCORRECT_USAGE.code);
        this.usage = usage;
    }

    public String getUsage() {
        return usage;
    }
}
