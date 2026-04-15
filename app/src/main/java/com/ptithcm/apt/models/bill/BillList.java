package com.ptithcm.apt.models.bill;

import android.provider.ContactsContract;

import com.ptithcm.apt.enums.BillStatus;

import java.math.BigDecimal;
import java.util.Date;

public class BillList {
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
    private String dueDate;

    public BillList() {
    }

    public BillList(String apartmentName, BigDecimal waterFee, Integer billingMonth, Integer billingYear, BigDecimal electricityFee, Long id, BigDecimal managementFee, BigDecimal sanitationFee, BillStatus status, BigDecimal totalAmount, String dueDate) {
        this.apartmentName = apartmentName;
        this.waterFee = waterFee;
        this.billingMonth = billingMonth;
        this.billingYear = billingYear;
        this.electricityFee = electricityFee;
        this.id = id;
        this.managementFee = managementFee;
        this.sanitationFee = sanitationFee;
        this.status = status;
        this.totalAmount = totalAmount;
        this.dueDate = dueDate;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public BigDecimal getWaterFee() {
        return waterFee;
    }

    public void setWaterFee(BigDecimal waterFee) {
        this.waterFee = waterFee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public BigDecimal getSanitationFee() {
        return sanitationFee;
    }

    public void setSanitationFee(BigDecimal sanitationFee) {
        this.sanitationFee = sanitationFee;
    }

    public BigDecimal getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(BigDecimal managementFee) {
        this.managementFee = managementFee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getElectricityFee() {
        return electricityFee;
    }

    public void setElectricityFee(BigDecimal electricityFee) {
        this.electricityFee = electricityFee;
    }

    public Integer getBillingYear() {
        return billingYear;
    }

    public void setBillingYear(Integer billingYear) {
        this.billingYear = billingYear;
    }

    public Integer getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(Integer billingMonth) {
        this.billingMonth = billingMonth;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
