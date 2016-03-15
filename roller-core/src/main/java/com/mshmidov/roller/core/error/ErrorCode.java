package com.mshmidov.roller.core.error;

public enum ErrorCode {
    INCORRECT_USAGE(1),
    INCORRECT_DICE_EXPRESSION(2),
    UNKNOWN_TABLE(3),
    INCORRECT_TABLE_DEFINITION(4),
    INCORRECT_VARIABLE_NAME(5),
    UNKNOWN_VARIABLE(6),
    INTERNAL_ERROR(99);

    public final int code;

    ErrorCode(int code) {this.code = code;}
}
