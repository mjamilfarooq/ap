package com.afterpay.excercise.frauddetection.model;

public interface ITransactionListener {
    void listen(Transaction transaction);
}
