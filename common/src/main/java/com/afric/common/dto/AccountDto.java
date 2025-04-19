package com.afric.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountDto(
        String id,
        String userId,
        String accountNumber,
        BigDecimal balance,
        String currency,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) {
    // Static factory method
    public static AccountDto of(String id, String userId, String accountNumber,
                                BigDecimal balance, String currency,
                                LocalDateTime createdAt, LocalDateTime updatedAt,
                                String createdBy, String updatedBy) {
        return new AccountDto(id, userId, accountNumber, balance, currency,
                createdAt, updatedAt, createdBy, updatedBy);
    }
}