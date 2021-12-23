package com.krishiv.myislam.service;

/**
 * Created by Admin on 23-03-2018.
 */

class ServiceHeader {

    String key, value;

    public ServiceHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
