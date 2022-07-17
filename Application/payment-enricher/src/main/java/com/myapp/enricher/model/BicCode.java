package com.myapp.enricher.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("BicCode")
public class BicCode implements Serializable {

    @Id
    private String swift_code;

    private int id;
    private String bank;
    private String city;

    private String branch;

    public String getSwift_code() {
        return swift_code;
    }

    public void setSwift_code(String swift_code) {
        this.swift_code = swift_code;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
