package com.afric.common.exception;

public final class AuthenticationException extends BankingException {
    public AuthenticationException(String message) {
        super(message);
    }
}