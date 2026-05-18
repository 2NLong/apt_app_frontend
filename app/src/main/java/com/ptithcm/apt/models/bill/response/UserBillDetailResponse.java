package com.ptithcm.apt.models.bill.response;

import com.ptithcm.apt.enums.BillStatus;

import java.math.BigDecimal;

public class UserBillDetailResponse {
    private Long id;
    private Long apartment;
    private String apartmentName;
    private Integer billingMonth;
    private Integer billingYear;
    private BigDecimal electricityFee;
    private BigDecimal waterFee;
    private BigDecimal managementFee;
    private BigDecimal sanitationFee;
    private BigDecimal totalAmount;
    private BillStatus status;
    private String createdAt;
    private String paidAt;
    private String dueDate;

    public UserBillDetailResponse() {
    }

    public UserBillDetailResponse(Long apartment,
            String apartmentName,
            Integer billingMonth,
            Integer billingYear,
            String createdAt,
            BigDecimal waterFee,
            BigDecimal totalAmount,
            String dueDate,
            BigDecimal electricityFee,
            Long id,
            BigDecimal managementFee,
            String paidAt,
            BigDecimal sanitationFee,
            BillStatus status) {
        this.apartment = apartment;
        this.apartmentName = apartmentName;
        this.billingMonth = billingMonth;
        this.billingYear = billingYear;
        this.createdAt = createdAt;
        this.waterFee = waterFee;
        this.totalAmount = totalAmount;
        this.dueDate = dueDate;
        this.electricityFee = electricityFee;
        this.id = id;
        this.managementFee = managementFee;
        this.paidAt = paidAt;
        this.sanitationFee = sanitationFee;
        this.status = status;
    }

    public Long getApartment() {
        return apartment;
    }

    public void setApartment(Long apartment) {
        this.apartment = apartment;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getElectricityFee() {
        return electricityFee;
    }

    public void setElectricityFee(BigDecimal electricityFee) {
        this.electricityFee = electricityFee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(BigDecimal managementFee) {
        this.managementFee = managementFee;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public BigDecimal getSanitationFee() {
        return sanitationFee;
    }

    public void setSanitationFee(BigDecimal sanitationFee) {
        this.sanitationFee = sanitationFee;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getWaterFee() {
        return waterFee;
    }

    public void setWaterFee(BigDecimal waterFee) {
        this.waterFee = waterFee;
    }
}
