package com.myapp.root;

import java.io.Serializable;

public class InitiatedPayment implements Serializable {

    private String bicCode;
    private String currency;
    private long amount;

    public InitiatedPayment() {

    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "InitiatedPayment{" +
                "bicCode='" + bicCode + '\'' +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                '}';
    }
}
