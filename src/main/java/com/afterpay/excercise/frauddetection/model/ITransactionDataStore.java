package com.afterpay.excercise.frauddetection.model;


import java.util.Collection;

public interface ITransactionDataStore {
    void add(Transaction transaction);
    Collection<Record> fetchAll(String cardNumber);
}
