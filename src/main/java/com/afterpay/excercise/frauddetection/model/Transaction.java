package com.afterpay.excercise.frauddetection.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class Transaction {

    private String credit_card;
    private LocalDateTime time;
    private double amount;

    public Transaction(String credit_card, LocalDateTime time, double amount) {
        this.credit_card = credit_card;
        this.time = time;
        this.amount = amount;
    }

    public String getCredit_card() { return credit_card; }

    public void setCredit_card(String credit_card) { this.credit_card = credit_card; }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "credit_card=" + credit_card +
                ", time=" + time +
                ", amount=" + amount +
                '}';
    }

    public String toStringFormat()
    {
        return String.format("%s, %s, %.2f", credit_card,
                time.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                amount);
    }
}
