package com.mshmidov.roller.core.error;

public final class IncorrectUsageException extends AbnormalExitException {

    private static final int CODE = 1;

    private final String usage;

    public IncorrectUsageException(String message, String usage) {
        super(message, CODE);
        this.usage = usage;
    }

    public IncorrectUsageException(String message, Throwable cause, String usage) {
        super(message, cause, CODE);
        this.usage = usage;
    }

    public IncorrectUsageException(Throwable cause, String usage) {
        super(cause, CODE);
        this.usage = usage;
    }


    public String getUsage() {
        return usage;
    }
}
