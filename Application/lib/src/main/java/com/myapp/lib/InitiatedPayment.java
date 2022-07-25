package com.myapp.lib;

import java.io.Serializable;

public class InitiatedPayment implements Serializable {

    private String internalParty;
    private String externalParty;
    private String direction;
    private String currency;
    private String timestamp;
    private long amount;

    public InitiatedPayment() {
    }

    public String getInternalParty() {
        return internalParty;
    }

    public void setInternalParty(String internalParty) {
        this.internalParty = internalParty;
    }

    public String getExternalParty() {
        return externalParty;
    }

    public void setExternalParty(String externalParty) {
        this.externalParty = externalParty;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "InitiatedPayment [amount=" + amount + ", currency=" + currency + ", direction=" + direction
                + ", externalParty=" + externalParty + ", internalParty=" + internalParty + ", timestamp=" + timestamp
                + "]";
    }
    
}
