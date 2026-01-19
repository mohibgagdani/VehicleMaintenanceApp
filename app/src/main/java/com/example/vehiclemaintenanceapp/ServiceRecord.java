package com.example.vehiclemaintenanceapp;

public class ServiceRecord {
    private String date;
    private String type;
    private String cost;
    private String notes;

    public ServiceRecord(String date, String type, String cost, String notes) {
        this.date = date;
        this.type = type;
        this.cost = cost;
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
