package com.ptithcm.apt.models.complaint;

public class ComplaintResponse {
    private Long id;
    private String category;
    private String title;
    private String content;
    private String status;
    private Long apartmentId;
    private String roomNumber;
    private Long residentId;
    private String residentName;
    private String createdAt;
    private String resolvedAt;

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getResidentName() {
        return residentName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getResolvedAt() {
        return resolvedAt;
    }
}
