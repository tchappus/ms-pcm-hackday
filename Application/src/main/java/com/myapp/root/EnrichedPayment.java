package com.myapp.root;

import com.azure.spring.data.cosmos.core.mapping.Container;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Container(containerName = "payments")
public class EnrichedPayment implements Serializable {

    @Id
    private String id;

    private String bicCode;

    private String bankName;

    private String city;

    private String currency;
    private long amount;

    private String branch;

    public EnrichedPayment() {

    }

    public EnrichedPayment(InitiatedPayment initiatedPayment) {
        this.bicCode = initiatedPayment.getBicCode();
        this.currency = initiatedPayment.getCurrency();
        this.amount = initiatedPayment.getAmount();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "EnrichedPayment{" +
                "bicCode='" + bicCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", address='" + city + '\'' +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                '}';
    }
}
