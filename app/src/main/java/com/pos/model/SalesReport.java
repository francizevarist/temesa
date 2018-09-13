package com.pos.model;

public class SalesReport {

    private String Username;
    private String Amount;
    private String Count;
    private String Timestamp;

    public SalesReport() {
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
