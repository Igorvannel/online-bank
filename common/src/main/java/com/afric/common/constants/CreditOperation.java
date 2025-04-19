package com.afric.common.constants;


// Base sealed interface for operation types
// Credit operation implementation
public final class CreditOperation implements OperationType {
    private static final CreditOperation INSTANCE = new CreditOperation();

    private CreditOperation() {}

    public static CreditOperation getInstance() {
        return INSTANCE;
    }

    @Override
    public String name() {
        return "CREDIT";
    }

    @Override
    public String getDescription() {
        return "Adds funds to an account";
    }
}

