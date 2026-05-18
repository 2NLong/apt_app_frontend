package com.ptithcm.apt.models.contract;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContractRequest {
    private Long apartmentId;
    private String role;
    private String email;
    private String citizenIdentity;
    private String fullName;
    private String dob;
    private String phone;
    private String contractStart;
    private String contractEnd;
    private BigDecimal rentalPrice;
    private BigDecimal depositAmount;

    public ContractRequest(Long apartmentId, String role, String email, String citizenIdentity,
                           String fullName, String dob, String phone,
                           String contractStart, String contractEnd,
                           BigDecimal rentalPrice, BigDecimal depositAmount) {
        this.apartmentId = apartmentId;
        this.role = role;
        this.email = email;
        this.citizenIdentity = citizenIdentity;
        this.fullName = fullName;
        this.dob = dob;
        this.phone = phone;
        this.contractStart = contractStart;
        this.contractEnd = contractEnd;
        this.rentalPrice = rentalPrice;
        this.depositAmount = depositAmount;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCitizenIdentity(String citizenIdentity) {
        this.citizenIdentity = citizenIdentity;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setContractStart(String contractStart) {
        this.contractStart = contractStart;
    }

    public void setContractEnd(String contractEnd) {
        this.contractEnd = contractEnd;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getCitizenIdentity() {
        return citizenIdentity;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDob() {
        return dob;
    }

    public String getPhone() {
        return phone;
    }

    public String getContractStart() {
        return contractStart;
    }

    public String getContractEnd() {
        return contractEnd;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }
}