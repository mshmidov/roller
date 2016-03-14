package com.mshmidov.roller.cli.error;

public abstract class AbnormalExitException extends RuntimeException {

    private final int code;

    public AbnormalExitException(String message, int code) {
        super(message);
        this.code = code;
    }

    public AbnormalExitException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public AbnormalExitException(Throwable cause, int code) {
        super(cause.getMessage(), cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
