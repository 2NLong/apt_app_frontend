package com.ptithcm.apt.models.bill;

import com.ptithcm.apt.enums.BillStatus;

import java.math.BigDecimal;

public class AdminBillDetail {
    private Long id;
    private Long apartmentId;
    private String apartmentName;
    private String apartmentFloor;
    private BigDecimal apartmentArea;
    private Integer billingMonth;
    private Integer billingYear;
    private BigDecimal electricityFee;
    private BigDecimal waterFee;
    private BigDecimal managementFee;
    private BigDecimal sanitationFee;
    private BigDecimal totalAmount;
    private BillStatus status;
    private String createdAt;
    private String createdBy;
    private String confirmBy;

    private String paidAt;
    private String dueDate;

    public AdminBillDetail() {
    }

    public AdminBillDetail(BigDecimal apartmentArea, String apartmentFloor, Long apartmentId, String apartmentName, Integer billingMonth, Integer billingYear, String confirmBy, String createdAt, String createdBy, String dueDate, BigDecimal electricityFee, Long id, BigDecimal managementFee, BigDecimal sanitationFee, BillStatus status, BigDecimal totalAmount, BigDecimal waterFee, String paidAt) {
        this.apartmentArea = apartmentArea;
        this.apartmentFloor = apartmentFloor;
        this.apartmentId = apartmentId;
        this.apartmentName = apartmentName;
        this.billingMonth = billingMonth;
        this.billingYear = billingYear;
        this.confirmBy = confirmBy;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.dueDate = dueDate;
        this.electricityFee = electricityFee;
        this.id = id;
        this.managementFee = managementFee;
        this.sanitationFee = sanitationFee;
        this.status = status;
        this.totalAmount = totalAmount;
        this.waterFee = waterFee;
        this.paidAt = paidAt;
    }

    public BigDecimal getApartmentArea() {
        return apartmentArea;
    }

    public void setApartmentArea(BigDecimal apartmentArea) {
        this.apartmentArea = apartmentArea;
    }

    public String getApartmentFloor() {
        return apartmentFloor;
    }

    public void setApartmentFloor(String apartmentFloor) {
        this.apartmentFloor = apartmentFloor;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
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

    public String getConfirmBy() {
        return confirmBy;
    }

    public void setConfirmBy(String confirmBy) {
        this.confirmBy = confirmBy;
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

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }
}
