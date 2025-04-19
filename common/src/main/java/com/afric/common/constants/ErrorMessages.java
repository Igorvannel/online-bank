package com.afric.common.constants;


public class ErrorMessages {
    // Auth errors
    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "Email is already in use";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password";

    // Account errors
    public static final String ERROR_ACCOUNT_NOT_FOUND = "Account not found";
    public static final String ERROR_INSUFFICIENT_BALANCE = "Insufficient balance for this operation";
    public static final String ERROR_INVALID_AMOUNT = "Amount must be greater than zero";

    // General errors
    public static final String ERROR_UNAUTHORIZED = "Full authentication is required to access this resource";
    public static final String ERROR_ACCESS_DENIED = "Access denied: You don't have permission to access this resource";
    public static final String ERROR_VALIDATION = "Validation error";
    public static final String ERROR_UNEXPECTED = "An unexpected error occurred";
}