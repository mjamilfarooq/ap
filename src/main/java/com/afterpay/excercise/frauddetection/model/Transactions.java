package com.afterpay.excercise.frauddetection.model;

import java.util.ArrayList;

public class Transactions {


    private String cardNumber;
    private ArrayList<Record> transactions;

    public Transactions()
    {


    }

    public Transactions(String cardNumber, ArrayList<Record> transactions)
    {
        this.cardNumber = cardNumber;
        this.transactions = transactions;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public ArrayList<Record> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Record> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "cardNumber='" + cardNumber + '\'' +
                ", transactions=" + transactions +
                '}';
    }
}
