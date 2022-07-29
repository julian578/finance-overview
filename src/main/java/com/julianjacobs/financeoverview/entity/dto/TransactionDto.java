package com.julianjacobs.financeoverview.entity.dto;

import java.util.Date;

public class TransactionDto {

    private String subject;
    private double value;


    public TransactionDto() {}

    public TransactionDto(String subject, double value) {
        this.subject = subject;
        this.value = value;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


}
