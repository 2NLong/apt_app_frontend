package com.ptithcm.apt.models.auth.rentinvoice;

import com.ptithcm.apt.enums.RentStatus;

import java.math.BigDecimal;

public class RentInvoiceList {
    private Long id;
    private String apartmentName;
    private Integer billingMonth;
    private Integer billingYear;
    private BigDecimal rentAmount;
    private RentStatus status;
    private String dueDate;

    public RentInvoiceList() {
    }

    public RentInvoiceList(String apartmentName, Integer billingMonth, Integer billingYear, String dueDate, Long id, BigDecimal rentAmount, RentStatus status) {
        this.apartmentName = apartmentName;
        this.billingMonth = billingMonth;
        this.billingYear = billingYear;
        this.dueDate = dueDate;
        this.id = id;
        this.rentAmount = rentAmount;
        this.status = status;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(BigDecimal rentAmount) {
        this.rentAmount = rentAmount;
    }

    public RentStatus getStatus() {
        return status;
    }

    public void setStatus(RentStatus status) {
        this.status = status;
    }
}
