package com.afric.common.exception;

public final class ValidationException extends BankingException {
    private final String field;

    public ValidationException(String field, String message) {
        super("Validation error for field '" + field + "': " + message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}