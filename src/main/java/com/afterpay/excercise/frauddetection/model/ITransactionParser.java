package com.afterpay.excercise.frauddetection.model;


import com.afterpay.excercise.frauddetection.exceptions.InvalidTransactionFormatException;


public interface ITransactionParser {
    Transaction parse(String transaction) throws InvalidTransactionFormatException;
}
