package com.ptithcm.apt.models;

public class Bill {
    private int id;   // 🔥 BẮT BUỘC

    private int apartmentId;
    private int month;
    private int year;

    private double electricityFee;
    private double waterFee;
    private double managementFee;
    private double sanitationFee;

    private double totalAmount;
    private String status;

    private String createdAt;
    private String paidAt;

    public Bill() {
    }

    public Bill(int apartmentId, String createdAt, int id, double electricityFee, double managementFee, int month, String paidAt, double sanitationFee, double waterFee, String status, double totalAmount, int year) {
        this.apartmentId = apartmentId;
        this.createdAt = createdAt;
        this.id = id;
        this.electricityFee = electricityFee;
        this.managementFee = managementFee;
        this.month = month;
        this.paidAt = paidAt;
        this.sanitationFee = sanitationFee;
        this.waterFee = waterFee;
        this.status = status;
        this.totalAmount = totalAmount;
        this.year = year;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getElectricityFee() {
        return electricityFee;
    }

    public void setElectricityFee(double electricityFee) {
        this.electricityFee = electricityFee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(double managementFee) {
        this.managementFee = managementFee;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getSanitationFee() {
        return sanitationFee;
    }

    public void setSanitationFee(double sanitationFee) {
        this.sanitationFee = sanitationFee;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getWaterFee() {
        return waterFee;
    }

    public void setWaterFee(double waterFee) {
        this.waterFee = waterFee;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

