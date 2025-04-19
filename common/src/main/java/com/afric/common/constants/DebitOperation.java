package com.afric.common.constants;

// Debit operation implementation
public final class DebitOperation implements OperationType {
    private static final DebitOperation INSTANCE = new DebitOperation();

    private DebitOperation() {
    }

    public static DebitOperation getInstance() {
        return INSTANCE;
    }

    @Override
    public String name() {
        return "DEBIT";
    }

    @Override
    public String getDescription() {
        return "Withdraws funds from an account";
    }
}
