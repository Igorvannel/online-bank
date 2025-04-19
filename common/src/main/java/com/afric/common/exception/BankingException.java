package com.afric.common.exception;


// Base sealed class for banking exceptions
public sealed abstract class BankingException extends RuntimeException
        permits ResourceNotFoundException, InsufficientBalanceException, DuplicateResourceException,
        AuthenticationException, ValidationException {

    public BankingException(String message) {
        super(message);
    }

    public BankingException(String message, Throwable cause) {
        super(message, cause);
    }
}