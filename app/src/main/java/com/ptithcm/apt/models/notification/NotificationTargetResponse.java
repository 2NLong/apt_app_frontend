package com.ptithcm.apt.models.notification;

public class NotificationTargetResponse {
    private Long apartmentId;
    private String roomNumber;
    private String residentName;
    private String residentEmail;

    public Long getApartmentId() {
        return apartmentId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getResidentName() {
        return residentName;
    }

    public String getResidentEmail() {
        return residentEmail;
    }
}
