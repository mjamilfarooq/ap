package com.afterpay.excercise.frauddetection.exceptions;

import com.afterpay.excercise.frauddetection.model.Transaction;

public class FraudulentTransactionException extends Exception {
    private Transaction transaction;

    public FraudulentTransactionException(Transaction transaction)
    {
        super("Fraudulent Transaction Detected " + transaction.toString());
        this.transaction = transaction;
    }

    public Transaction getTransaction()
    {
        return transaction;
    }
}
