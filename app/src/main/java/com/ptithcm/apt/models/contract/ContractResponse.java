package com.ptithcm.apt.models.contract;

public class ContractResponse {
    private Long id;
    private String roomNumber;
    private String residentName;
    private String citizenIdentity;
    private String phone;
    private String role;
    private Boolean isHead;
    private Double rentalPrice;
    private Double depositAmount;
    private String contractStart;
    private String contractEnd;
    private Boolean isActive;

    public Long getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public String getResidentName() { return residentName; }
    public String getRole() { return role; }
    public String getContractStart() { return contractStart; }
    public String getContractEnd() { return contractEnd; }
}