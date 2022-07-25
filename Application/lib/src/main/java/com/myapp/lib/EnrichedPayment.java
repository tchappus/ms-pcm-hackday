package com.myapp.lib;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Container(containerName = "payments")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnrichedPayment implements Serializable {

    @Id
    private String id;
    private String direction;
    private String currency;
    private String timestamp;
    private long amount;
    private Party internalParty;
    private Party externalParty;
    
    public EnrichedPayment() {
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Party getInternalParty() {
        return internalParty;
    }

    public void setInternalParty(Party internalParty) {
        this.internalParty = internalParty;
    }

    public Party getExternalParty() {
        return externalParty;
    }

    public void setExternalParty(Party externalParty) {
        this.externalParty = externalParty;
    }

    @Override
    public String toString() {
        return "EnrichedPayment [amount=" + amount + ", currency=" + currency + ", direction=" + direction
                + ", externalParty=" + externalParty + ", id=" + id + ", internalParty=" + internalParty
                + ", timestamp=" + timestamp + "]";
    }
    
}
