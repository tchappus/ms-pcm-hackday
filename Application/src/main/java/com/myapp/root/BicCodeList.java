package com.myapp.root;

import java.util.List;

public class BicCodeList {
    String country;
    String country_code;
    List<BicCode> list;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public List<BicCode> getList() {
        return list;
    }

    public void setList(List<BicCode> list) {
        this.list = list;
    }
}