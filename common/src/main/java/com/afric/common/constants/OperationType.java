package com.afric.common.constants;


public sealed interface OperationType permits CreditOperation, DebitOperation {
    String name();
    String getDescription();

    // Méthode statique pour obtenir une instance de CreditOperation
    static OperationType credit() {
        return CreditOperation.getInstance();
    }

    // Méthode statique pour obtenir une instance de DebitOperation
    static OperationType debit() {
        return DebitOperation.getInstance();
    }
}