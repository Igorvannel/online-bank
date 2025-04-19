package com.afric.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequestDto(
        @NotBlank String accountId,
        @NotNull @Positive BigDecimal amount
) {
    // Compact constructor for validation
    public TransactionRequestDto {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    // Static factory method
    public static TransactionRequestDto of(String accountId, BigDecimal amount) {
        return new TransactionRequestDto(accountId, amount);
    }
}