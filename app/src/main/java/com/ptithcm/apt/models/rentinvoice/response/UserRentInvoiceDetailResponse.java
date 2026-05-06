package com.ptithcm.apt.models.rentinvoice.response;

public class UserRentInvoiceDetailResponse {
    private int id;

    private int apartment;

    private String apartmentName;

    private int billingMonth;

    private int billingYear;

    private double rentAmount;

    private String status;

    private String createdAt;

    private String paidAt;

    private String dueDate;

    public UserRentInvoiceDetailResponse() {
    }

    public UserRentInvoiceDetailResponse(int apartment,
            String apartmentName,
            int billingMonth,
            int billingYear,
            String createdAt,
            String dueDate,
            int id,
            String paidAt,
            double rentAmount,
            String status) {
        this.apartment = apartment;
        this.apartmentName = apartmentName;
        this.billingMonth = billingMonth;
        this.billingYear = billingYear;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.id = id;
        this.paidAt = paidAt;
        this.rentAmount = rentAmount;
        this.status = status;
    }

    public int getApartment() {
        return apartment;
    }

    public void setApartment(int apartment) {
        this.apartment = apartment;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public int getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(int billingMonth) {
        this.billingMonth = billingMonth;
    }

    public int getBillingYear() {
        return billingYear;
    }

    public void setBillingYear(int billingYear) {
        this.billingYear = billingYear;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public double getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(double rentAmount) {
        this.rentAmount = rentAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
