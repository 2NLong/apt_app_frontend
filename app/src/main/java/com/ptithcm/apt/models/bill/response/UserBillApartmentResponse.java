package com.ptithcm.apt.models.bill.response;

public class UserBillApartmentResponse {
    private Long apartmentId;
    private String roomNumber;
    private String role;
    private boolean isHead;

    public UserBillApartmentResponse() {
    }

    public UserBillApartmentResponse(Long apartmentId,
            String roomNumber,
            String role,
            boolean isHead) {
        this.apartmentId = apartmentId;
        this.roomNumber = roomNumber;
        this.role = role;
        this.isHead = isHead;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean head) {
        isHead = head;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return roomNumber;
    }
}
