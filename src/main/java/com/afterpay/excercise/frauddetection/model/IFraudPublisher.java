package com.afterpay.excercise.frauddetection.model;

public interface IFraudPublisher {
    void publish(Transactions transaction);
}
