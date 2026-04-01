package com.ptithcm.apt.models.profileuser;

import java.io.Serializable;

public class Apartment implements Serializable {
    private Integer id;
    private String roomNumber;
    private Integer floor;
    private Double area;
    private String status; // 'AVAILABLE', 'RENTED', 'OWNED'

    private String role; // 'OWNER', 'TENANT', 'MEMBER'
    private Boolean isHead;
    private String contractStart;
    private String contractEnd;

    private int iconResId;

    public Apartment() {
    }

    // Constructor cũ
    public Apartment(String roomNumber, String status, int iconResId) {
        this.roomNumber = roomNumber;
        this.status = status;
        this.iconResId = iconResId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getName() {
        return roomNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsHead() {
        return isHead;
    }

    public void setIsHead(Boolean head) {
        isHead = head;
    }

    public String getContractStart() {
        return contractStart;
    }

    public void setContractStart(String contractStart) {
        this.contractStart = contractStart;
    }

    public String getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(String contractEnd) {
        this.contractEnd = contractEnd;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
