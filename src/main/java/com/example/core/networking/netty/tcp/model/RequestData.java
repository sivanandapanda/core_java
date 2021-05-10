package com.example.core.networking.netty.tcp.model;

public class RequestData {
    private int intValue;

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public void setStringValue(String toString) {
        this.intValue = Integer.parseInt(toString);
    }

    public String getStringValue() {
        return String.valueOf(intValue);
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "intValue=" + intValue +
                '}';
    }
}