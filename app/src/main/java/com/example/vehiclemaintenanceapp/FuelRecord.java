package com.example.vehiclemaintenanceapp;

public class FuelRecord {
    private String date;
    private String cost;

    public FuelRecord(String date, String cost) {
        this.date = date;
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
