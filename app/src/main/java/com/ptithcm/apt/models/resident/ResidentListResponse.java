package com.ptithcm.apt.models.resident;

import java.io.Serializable;

public class ResidentListResponse implements Serializable {
    private Long residentId;
    private String fullName;
    private String citizenIdentity;
    private String phone;
    private String roomNumber;
    private String role;
    private Boolean isHead;
    private String contractStart;

    public ResidentListResponse() {
    }

    // Getters and Setters
    public Long getResidentId() { return residentId; }
    public void setResidentId(Long residentId) { this.residentId = residentId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCitizenIdentity() { return citizenIdentity; }
    public void setCitizenIdentity(String citizenIdentity) { this.citizenIdentity = citizenIdentity; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getIsHead() { return isHead; }
    public void setIsHead(Boolean isHead) { this.isHead = isHead; }

    public String getContractStart() { return contractStart; }
    public void setContractStart(String contractStart) { this.contractStart = contractStart; }
}