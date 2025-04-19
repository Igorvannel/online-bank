package com.afric.common.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
        String id,
        String accountId,
        BigDecimal amount,
        String direction,
        BigDecimal balanceBefore,
        BigDecimal balanceAfter,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) {
    // Static factory method
    public static TransactionDto of(String id, String accountId, BigDecimal amount,
                                    String direction, BigDecimal balanceBefore, BigDecimal balanceAfter,
                                    LocalDateTime createdAt, LocalDateTime updatedAt,
                                    String createdBy, String updatedBy) {
        return new TransactionDto(id, accountId, amount, direction, balanceBefore, balanceAfter,
                createdAt, updatedAt, createdBy, updatedBy);
    }
}