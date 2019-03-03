package com.afterpay.excercise.frauddetection.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Record {


    private LocalDateTime date;

    private Double amount;


    public Record()
    {}

    public Record(LocalDateTime date, Double amount)
    {
        this.date = date;
        this.amount = amount;
    }

    @JsonIgnore
    public LocalDateTime getDate() {
        return date;
    }

    @JsonIgnore
    public void setDate(LocalDateTime date) {
        this.date = date;
    }


    @JsonProperty
    public String getDateString() { return date.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);}

    @JsonProperty
    public void setDateString(String date) { this.date = LocalDateTime.parse(date); }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "date=" + date +
                ", amount=" + amount +
                '}';
    }
}
