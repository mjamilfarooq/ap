package com.afterpay.excercise.frauddetection.exceptions;

public class InvalidTransactionFormatException extends Exception {
    public InvalidTransactionFormatException(String transaction)
    {
        super("InvalidTransactionFormatException " + transaction);
    }
}
