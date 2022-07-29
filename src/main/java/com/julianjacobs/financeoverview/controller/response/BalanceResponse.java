package com.julianjacobs.financeoverview.controller.response;

public class BalanceResponse {

    double balance;

    public BalanceResponse(double balance) {
        this.balance = balance;
    }

    public BalanceResponse() {}

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
