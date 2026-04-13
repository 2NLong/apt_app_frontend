package com.ptithcm.apt.models.bill;

public class Bill {
    private int id;

    private int apartmentId;
    private int apartmentName;
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

}