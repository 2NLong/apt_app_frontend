package com.ptithcm.apt.models.apartment;

public class Apartment {
    private Long id;
    private String roomNumber; // Backend dùng roomNumber thay vì name
    private Integer floor;
    private Double area;       // BigDecimal ở BE map sang Double ở Android
    private String status;
    private String createdAt;  // LocalDateTime map sang String cho dễ hiển thị

    // Bạn dùng Alt + Insert để tạo Constructor, Getter và Setter cho các trường này nhé


    public Long getId() {
        return id;
    }
    public String getRoomNumber() { return roomNumber; }
    public String getStatus() { return status; }
    public Integer getFloor() { return floor; }
    public Double getArea() { return area; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public void setArea(Double area) {
        this.area = area;
    }
}