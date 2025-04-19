package com.afric.common.exception;

import java.math.BigDecimal;

public final class InsufficientBalanceException extends BankingException {
    private final String accountId;
    private final BigDecimal requested;
    private final BigDecimal available;

    public InsufficientBalanceException(String accountId, BigDecimal requested, BigDecimal available) {
        super("Insufficient balance in account " + accountId +
                ": requested " + requested + ", available " + available);
        this.accountId = accountId;
        this.requested = requested;
        this.available = available;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getRequested() {
        return requested;
    }

    public BigDecimal getAvailable() {
        return available;
    }
}