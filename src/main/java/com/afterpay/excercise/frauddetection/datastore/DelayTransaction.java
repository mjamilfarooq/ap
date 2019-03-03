package com.afterpay.excercise.frauddetection.datastore;

import com.afterpay.excercise.frauddetection.model.Transaction;

import java.time.LocalDateTime;

public class DelayTransaction {

    private Transaction transaction;
    private LocalDateTime expiryTime;


    public DelayTransaction(Transaction transaction, int delayHrs)
    {
        this.transaction = transaction;
        LocalDateTime date = transaction.getTime();
        expiryTime = date.plusHours(delayHrs);
    }

    Transaction getTransaction()
    {
        return transaction;
    }

    public boolean isExpired()
    {
        return expiryTime.isBefore(LocalDateTime.now());
    }

}
