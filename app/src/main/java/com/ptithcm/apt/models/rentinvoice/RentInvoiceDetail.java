package com.ptithcm.apt.models.rentinvoice;

import java.math.BigDecimal;

public class RentInvoiceDetail {
    private Long id;
    private String apartmentName;
    private String apartmentFloor;
    private Double apartmentArea;
    private Integer billingMonth;
    private Integer billingYear;
    private BigDecimal rentAmount;
    private String tenantName;
    private String ownerName;
    private String status;
    private String createdBy;
    private String createdAt;
    private String dueDate;

    public RentInvoiceDetail() {
    }

    public RentInvoiceDetail(Double apartmentArea, String apartmentFloor, String apartmentName, Integer billingMonth, Integer billingYear, String createdAt, String dueDate, String createdBy, Long id, String ownerName, BigDecimal rentAmount, String status, String tenantName) {
        this.apartmentArea = apartmentArea;
        this.apartmentFloor = apartmentFloor;
        this.apartmentName = apartmentName;
        this.billingMonth = billingMonth;
        this.billingYear = billingYear;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.createdBy = createdBy;
        this.id = id;
        this.ownerName = ownerName;
        this.rentAmount = rentAmount;
        this.status = status;
        this.tenantName = tenantName;
    }

    public Double getApartmentArea() {
        return apartmentArea;
    }

    public void setApartmentArea(Double apartmentArea) {
        this.apartmentArea = apartmentArea;
    }

    public String getApartmentFloor() {
        return apartmentFloor;
    }

    public void setApartmentFloor(String apartmentFloor) {
        this.apartmentFloor = apartmentFloor;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public Integer getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(Integer billingMonth) {
        this.billingMonth = billingMonth;
    }

    public Integer getBillingYear() {
        return billingYear;
    }

    public void setBillingYear(Integer billingYear) {
        this.billingYear = billingYear;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(BigDecimal rentAmount) {
        this.rentAmount = rentAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
}
