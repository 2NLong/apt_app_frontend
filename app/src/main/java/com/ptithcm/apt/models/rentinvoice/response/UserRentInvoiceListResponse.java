package com.ptithcm.apt.models.rentinvoice.response;

import com.ptithcm.apt.enums.RentStatus;

public class UserRentInvoiceListResponse {
    private Long id;
    private String apartmentName;
    private Integer billingMonth;
    private Integer billingYear;
    private Double rentAmount;
    private RentStatus status;
    private String dueDate;
    private String viewerRole;
    private String tenantName;

    public UserRentInvoiceListResponse() {
    }

    public UserRentInvoiceListResponse(String apartmentName,
            Integer billingMonth,
            Integer billingYear,
            String dueDate,
            Long id,
            Double rentAmount,
            RentStatus status,
            String tenantName,
            String viewerRole) {
        this.apartmentName = apartmentName;
        this.billingMonth = billingMonth;
        this.billingYear = billingYear;
        this.dueDate = dueDate;
        this.id = id;
        this.rentAmount = rentAmount;
        this.status = status;
        this.tenantName = tenantName;
        this.viewerRole = viewerRole;
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

    public Double getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(Double rentAmount) {
        this.rentAmount = rentAmount;
    }

    public RentStatus getStatus() {
        return status;
    }

    public void setStatus(RentStatus status) {
        this.status = status;
    }

    public String getViewerRole() {
        return viewerRole;
    }

    public void setViewerRole(String viewerRole) {
        this.viewerRole = viewerRole;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
}
