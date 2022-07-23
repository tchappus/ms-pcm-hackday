package com.myapp.lib;

import java.io.Serializable;

public class Party implements Serializable {
    private String bicCode;
    private String city;
    private String branch;
    private String bankName;

    public Party() {
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return "Party [bankName=" + bankName + ", bicCode=" + bicCode + ", branch=" + branch + ", city=" + city + "]";
    }
    
}
