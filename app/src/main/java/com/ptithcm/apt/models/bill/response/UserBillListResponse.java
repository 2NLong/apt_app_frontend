package com.ptithcm.apt.models.bill.response;

import com.ptithcm.apt.enums.BillStatus;

import java.math.BigDecimal;

public class UserBillListResponse {
    private Long id;
    private String apartmentName;
    private Integer billingMonth;
    private Integer billingYear;
    private BigDecimal electricityFee;
    private BigDecimal waterFee;
    private BigDecimal managementFee;
    private BigDecimal sanitationFee;
    private BigDecimal totalAmount;
    private BillStatus status;
    private String viewerRole;
    private String tenantName;
    private String dueDate;

    public UserBillListResponse() {
    }

    public UserBillListResponse(String apartmentName,
            Integer billingMonth,
            Integer billingYear,
            String dueDate,
            BigDecimal electricityFee,
            Long id,
            BigDecimal managementFee,
            BigDecimal sanitationFee,
            BillStatus status,
            String tenantName,
            BigDecimal totalAmount,
            String viewerRole,
            BigDecimal waterFee) {
        this.apartmentName = apartmentName;
        this.billingMonth = billingMonth;
        this.billingYear = billingYear;
        this.dueDate = dueDate;
        this.electricityFee = electricityFee;
        this.id = id;
        this.managementFee = managementFee;
        this.sanitationFee = sanitationFee;
        this.status = status;
        this.tenantName = tenantName;
        this.totalAmount = totalAmount;
        this.viewerRole = viewerRole;
        this.waterFee = waterFee;
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

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getViewerRole() {
        return viewerRole;
    }

    public void setViewerRole(String viewerRole) {
        this.viewerRole = viewerRole;
    }

    public BigDecimal getWaterFee() {
        return waterFee;
    }

    public void setWaterFee(BigDecimal waterFee) {
        this.waterFee = waterFee;
    }
}