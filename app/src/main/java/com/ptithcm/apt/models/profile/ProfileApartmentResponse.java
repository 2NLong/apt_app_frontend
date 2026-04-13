package com.ptithcm.apt.models.profile;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProfileApartmentResponse implements Serializable {
    private Long apartmentId;
    private String roomNumber;
    private Integer floor;
    private BigDecimal area;
    private String role;
    private Boolean isHead;
    private String contractStart;
    private String contractEnd;
    private BigDecimal rentalPrice;
    private BigDecimal depositAmount;

    public Long getApartmentId() { return apartmentId; }
    public String getRoomNumber() { return roomNumber; }
    public Integer getFloor() { return floor; }
    public BigDecimal getArea() { return area; }
    public String getRole() { return role; }
    public Boolean getIsHead() { return isHead; }
    public String getContractStart() { return contractStart; }
    public String getContractEnd() { return contractEnd; }
    public BigDecimal getRentalPrice() { return rentalPrice; }
    public BigDecimal getDepositAmount() { return depositAmount; }
}
