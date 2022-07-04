package com.myapp.root;

import java.io.Serializable;

public class Payment implements Serializable {



    private String currency;
    private long amount;

    public Payment() {

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
        return "Payment{" +
                "currency='" + currency + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
